package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class ClassEntity {

    @Id
    private Integer id;
    @Column(nullable = false)
    private String name;

    @Column(name = "degree_id", nullable = false)
    private Integer degreeId;

    @ManyToOne // Uma Turma pertence a UMA Serie
    @JoinColumn(name = "degree_id", insertable = false, updatable = false)
    private Degree degree;




}
