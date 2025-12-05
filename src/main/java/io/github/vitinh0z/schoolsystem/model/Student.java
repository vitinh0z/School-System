package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class Student {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "class_id", nullable = false)
    private Integer degreeId;

    @OneToOne(optional = false)
    private ClassEntity classEntity;



}
