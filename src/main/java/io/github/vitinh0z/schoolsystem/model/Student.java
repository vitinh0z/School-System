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
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Spring Boot gera automaticamente um Id no banco de dados
    private Integer id;

    @Column(name = "class_id", nullable = false) /* por padrão, o Jpa da nome da
    coluna com base no nome do atributo da classe
    */
    @NotNull(message = "Classe não pode ser nula")
    @Min(value = 1, message = "ID da classe deve ser valido")
    private Integer classId;

    @NotBlank(message = "Nome é Obrigatorio")
    @Column(nullable = false)
    private String name;

    @ManyToOne // Muitos aluno para UMA Classe
    @JoinColumn(name = "class_id", insertable = false, updatable = false)
    private ClassEntity classEntity;



}
