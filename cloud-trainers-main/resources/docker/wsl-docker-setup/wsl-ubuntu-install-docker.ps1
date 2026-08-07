# Idempotent WSL + Docker bootstrap for Ubuntu.
$ErrorActionPreference = 'Stop'

$DistroName = 'Ubuntu-26.04'
$BaseDistro = 'Ubuntu-26.04'
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

function Test-PendingRestart
{
  $pendingRebootKeys = @(
    "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Component Based Servicing\RebootPending",
    "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\WindowsUpdate\Auto Update\RebootRequired",
    "HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\PendingFileRenameOperations"
  )

  foreach ($key in $pendingRebootKeys)
  {
    if (Test-Path $key)
    {
      return $true
    }
  }

  # Check for pending computer rename
  $computerName = $env:COMPUTERNAME
  $activeComputerName = (Get-ItemProperty -Path "HKLM:\SYSTEM\CurrentControlSet\Control\ComputerName\ActiveComputerName" -Name "ComputerName").ComputerName
  $pendingComputerName = (Get-ItemProperty -Path "HKLM:\SYSTEM\CurrentControlSet\Control\ComputerName\ComputerName" -Name "ComputerName").ComputerName

  if ($computerName -ne $activeComputerName -or $activeComputerName -ne $pendingComputerName)
  {
    return $true
  }

  return $false
}


# Step 1: Enable Virtual Machine Platform
Write-Host "Enabling Virtual Machine Platform... This may take a while..." -ForegroundColor Cyan
try
{
  # Check if a restart is already pending before making changes
  if (Test-PendingRestart)
  {
    Write-Warning "A system restart is pending. Please restart your laptop and run this script again."
    Write-Host "This will ensure all previous system changes are properly applied before continuing." -ForegroundColor Yellow
    Write-Host "`nPress Enter to exit..." -ForegroundColor Yellow
    Read-Host
    exit
  }

  $vmPlatformState = (Get-WindowsOptionalFeature -Online -FeatureName VirtualMachinePlatform).State
  if ($vmPlatformState -eq "Enabled")
  {
    Write-Host "Virtual Machine Platform is already enabled." -ForegroundColor Green
  }
  else
  {
    $maxRetries = 1
    $retryCount = 0
    $success = $false

    while (-not $success -and $retryCount -le $maxRetries)
    {
      try
      {
        $job = Start-Job -ScriptBlock {
          Enable-WindowsOptionalFeature -Online -FeatureName VirtualMachinePlatform -All -NoRestart
        }

        # Wait for the job to complete with a 2-minute timeout
        $completed = Wait-Job -Job $job -Timeout 120

        if ($null-eq $completed)
        {
          # Job timed out
          Write-Warning "Enabling Virtual Machine Platform timed out after 2 minutes."
          Remove-Job -Job $job -Force

          if ($retryCount -lt $maxRetries)
          {
            $retryCount++
            Write-Host "Retrying Step 1 (attempt $retryCount of $maxRetries)..." -ForegroundColor Yellow
            continue
          }
          else
          {
            throw "Failed to enable Virtual Machine Platform after $maxRetries retries."
          }
        }

        # Get the job result
        $result = Receive-Job -Job $job
        Remove-Job -Job $job

        # If we got here, the operation was successful
        $success = $true
        Start-Sleep -Seconds 10
        Write-Host "Virtual Machine Platform has been enabled." -ForegroundColor Green
      }
      catch
      {
        if ($retryCount -lt $maxRetries)
        {
          $retryCount++
          Write-Host "Error enabling Virtual Machine Platform: $_" -ForegroundColor Red
          Write-Host "Retrying Step 1 (attempt $retryCount of $maxRetries)..." -ForegroundColor Yellow
        }
        else
        {
          Write-Warning "Failed to enable Virtual Machine Platform after $maxRetries retries. Error: $_"
          throw
        }
      }
    }

    # Check if a restart is pending after enabling the feature
    if (Test-PendingRestart)
    {
      Write-Warning "A system restart is required to complete the Virtual Machine Platform installation."
      Write-Host "Please restart your laptop and run this script again." -ForegroundColor Yellow
      Write-Host "`nPress Enter to exit..." -ForegroundColor Yellow
      Read-Host
      exit
    }
    else
    {
      Write-Host "No restart is required at this time." -ForegroundColor Green
    }
  }
}
catch
{
  Write-Warning "Failed to enable Virtual Machine Platform. Error: $_"
  Write-Host "Please enable Virtual Machine Platform manually through Windows Features." -ForegroundColor Red
}

# Step 2: Download and install WSL
Write-Host "Checking if WSL is already installed..." -ForegroundColor Cyan
$wslInstalled = $false

# Try to run a WSL command to check if WSL is installed
try
{
  $wslOutput = wsl --status 2>&1
  if ($LASTEXITCODE -eq 0)
  {
    $wslInstalled = $true
    Write-Host "WSL is already installed. Skipping installation." -ForegroundColor Green
  }
}
catch
{
  # WSL is not installed, continue with installation
  $wslInstalled = $false
}

if (-not $wslInstalled)
{
  $wslInstallerUrl = "https://github.com/microsoft/WSL/releases/download/2.7.8/wsl.2.7.8.0.x64.msi"
  $wslInstallerPath = "$env:TEMP\wsl.2.7.8.0.x64.msi"

  Write-Host "Downloading WSL installer..." -ForegroundColor Cyan
  Invoke-WebRequest -Uri $wslInstallerUrl -OutFile $wslInstallerPath

  Write-Host "Installing WSL..." -ForegroundColor Cyan
  Start-Process -FilePath "msiexec.exe" -ArgumentList "/i `"$wslInstallerPath`" /quiet /norestart" -Wait
  Write-Host "WSL installation completed." -ForegroundColor Green
}

# Step 3: Set WSL 2 as default
Write-Host "Setting WSL 2 as default..." -ForegroundColor Cyan
wsl --set-default-version 2 | Out-Null
Write-Host "WSL 2 set as default." -ForegroundColor Green

# Step 4: Configure WSL resources
Write-Host "Configuring WSL resources..." -ForegroundColor Cyan
$wslConfigPath = "$env:USERPROFILE\.wslconfig"
$wslConfigContent = @"
[wsl2]
memory=16GB
processors=4
networkingMode=mirrored
"@

Set-Content -Path $wslConfigPath -Value $wslConfigContent
Write-Host "WSL resources configured." -ForegroundColor Green






# Install distro only if it does not already exist.
$ExistingDistros = wsl -l -q
if (-not ($ExistingDistros | Where-Object { $_.Trim() -eq $DistroName })) {
  wsl --install -d $BaseDistro --name $DistroName --no-launch
}

# Ensure distro is started before using \\wsl$ share.
wsl -d $DistroName -u root -- bash -lc "true"

# Certificates
wsl -d $DistroName -u root -- bash -lc "mkdir -p /usr/local/share/ca-certificates"
Copy-Item "$ScriptDir\certificates\*.crt" "\\wsl$\$DistroName\tmp\" -Force
wsl -d $DistroName -u root -- bash -lc "
cp /tmp/*.crt /usr/local/share/ca-certificates/
update-ca-certificates
"

# Update system packages every run
wsl -d $DistroName -u root -- bash -lc "
export DEBIAN_FRONTEND=noninteractive
apt-get update
apt-get -y upgrade
"

# Install/upgrade Docker packages every run  and Azure CLI
# https://docs.docker.com/engine/install/ubuntu/
wsl -d $DistroName -u root -- bash -lc '
export DEBIAN_FRONTEND=noninteractive
sudo apt update
sudo apt install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

sudo tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
EOF

sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo install -m 0755 -d /etc/docker
sudo tee /etc/docker/daemon.json <<EOF
{
  \"dns\": [\"8.8.8.8\", \"1.1.1.1\"]
}
EOF

curl -fsSL 'https://azurecliprod.blob.core.windows.net/$root/deb_install.sh' | sudo bash
'

# Initialize/maintain a sudoer user named ubuntu and configure WSL defaults/systemd.
# Docker service autostart requires systemd=true in /etc/wsl.conf.
wsl -d $DistroName -u root -- bash -lc "
getent group docker >/dev/null 2>&1 || groupadd docker

id -u ubuntu >/dev/null 2>&1 || useradd -m -G sudo,docker -s /bin/bash ubuntu
usermod -aG docker ubuntu

echo 'ubuntu:ubuntu' | chpasswd

echo '%sudo ALL=(ALL:ALL) ALL' > /etc/sudoers.d/sudo
chmod 440 /etc/sudoers.d/sudo

cat > /etc/wsl.conf <<EOF
[boot]
systemd=true

[user]
default=ubuntu
EOF
"

# Restart distro so systemd is active, then enable/start docker under systemd.
wsl --terminate $DistroName
wsl -d $DistroName -u root -- bash -lc "
systemctl enable --now docker
systemctl is-active docker
"

wsl --terminate $DistroName

# wsl --set-default Ubuntu-26.04
# wsl --unregister Ubuntu-26.04