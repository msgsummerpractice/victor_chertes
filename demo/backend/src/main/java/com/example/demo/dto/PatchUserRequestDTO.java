package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class PatchUserRequestDTO {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String firstName;

    private String lastName;

    public PatchUserRequestDTO() {
    }

    public PatchUserRequestDTO(String username, String email, String password, String firstname, String lastname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstname;
        this.lastName = lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PatchUserRequestDTO that = (PatchUserRequestDTO) o;
        return java.util.Objects.equals(username, that.username) &&
                java.util.Objects.equals(email, that.email) &&
                java.util.Objects.equals(password, that.password) &&
                java.util.Objects.equals(firstName, that.firstName) &&
                java.util.Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(username, email, password, firstName, lastName);
    }
}