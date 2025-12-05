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
public class Degree {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;



}
