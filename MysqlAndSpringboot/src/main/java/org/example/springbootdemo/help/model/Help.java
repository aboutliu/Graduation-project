package org.example.springbootdemo.help.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "helpinfo")
public class Help {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String introduction;
    private String time;
    private String place;
    private String phone;
    private String image;
    private String idname;
    private double longitude;
    private double latitude;
}