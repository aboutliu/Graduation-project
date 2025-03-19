package org.example.springbootdemo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import org.example.springbootdemo.register.UserRegisterRequest;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String username;
    private int id;
    private String name;
    private String password;
    private int age;
    private String phone;
    private String sex;
    private int root;
}