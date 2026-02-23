package com.milosz.cantor.auth;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
    
@Entity
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue
        private UUID id;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false)
        private String password;

        public User(String email ,String username, String password) {
            this.email = email;
            this.username = username;
            this.password = password;
        }

        public UUID getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String setPassword(String password) {
            this.password = password;
            return password;
        }

        public String setEmail(String email) {
            this.email = email;
            return email;
        }

            public String setUsername(String username) {
                this.username = username;
                return username;
            }
    
            public String getPassword() {
                return password;
            }

    public User() {
        
    }

}

