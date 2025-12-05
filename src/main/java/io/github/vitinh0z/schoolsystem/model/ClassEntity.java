package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class ClassEntity {

    @Id
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(name = "degree_id", nullable = false)
    private Integer degreeId;




}
