package com.example.minionlinemarket.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Admin extends User {
    // Additional fields specific to Admin can be added here
}
