package org.example.springbootdemo.activity.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "activityinfo")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String introduction;
    private String time;
    private String others;
}