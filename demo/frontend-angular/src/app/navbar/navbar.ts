import { Component,signal } from '@angular/core';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { IfAuth } from '../directives/if-auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatToolbarModule,MatButtonModule,MatIconModule,MatMenuModule,IfAuth],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  isUserLoggedIn = signal<boolean>(true);
}
