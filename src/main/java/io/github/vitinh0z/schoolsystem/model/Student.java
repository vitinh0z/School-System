package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Student {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "class_id", nullable = false)
    private Integer degreeId;

}
