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

    @Column(name = "class_id", nullable = false)
    private Integer classId;

    @Column(nullable = false)
    private String name;

    @ManyToOne // Muitos aluno para UMA Classe
    @JoinColumn(name = "class_id", insertable = false, updatable = false)
    private ClassEntity classEntity;



}
