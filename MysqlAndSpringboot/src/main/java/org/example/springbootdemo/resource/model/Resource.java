package org.example.springbootdemo.resource.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "resourceinfo")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String introduction;
    private String time;
    private String category;
    private String editor;
    private String image;
    private String idname;
}