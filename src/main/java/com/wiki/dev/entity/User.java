package com.wiki.dev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    @NotBlank(message = "password is required !")
    private String password;

    @Email
    @NotEmpty(message = "Email is required !")
    @Column(unique = true)
    private String email;

    private Instant created;

    private boolean enabled;
}
