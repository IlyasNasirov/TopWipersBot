package com.example.topwipersbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.glassfish.grizzly.http.util.TimeStamp;

@Entity(name = "users")
@Data
public class User {
    @Id
    private Long userId;
    private String firstname;
    private String surname;
    private String username;
    private String city;
    private TimeStamp registeredAt;
}
