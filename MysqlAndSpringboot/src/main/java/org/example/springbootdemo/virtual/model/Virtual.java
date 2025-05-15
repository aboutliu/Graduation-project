package org.example.springbootdemo.virtual.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "virtualinfo")
public class Virtual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String age;
    private String time;
    private String editor;
    private String introduction;
    private String image;
    private String idname;
    private Integer up;
    private Integer down;
}