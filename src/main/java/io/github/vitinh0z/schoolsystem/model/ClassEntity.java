package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class ClassEntity {

    @Id
    private Integer id;

    @NotBlank(message = "O nome da turma é obrigatório" )
    @Column(nullable = false)
    private String name;

    @NotNull(message = "É obrigatório selecionar uma série (Degree)")
    @Min(value = 1, message = "Deve ter ID Valido")
    @Column(name = "degree_id", nullable = false)
    private Integer degreeId;

    @ManyToOne // Uma Turma pertence a UMA Serie
    @JoinColumn(name = "degree_id", insertable = false, updatable = false)
    private Degree degree;




}
