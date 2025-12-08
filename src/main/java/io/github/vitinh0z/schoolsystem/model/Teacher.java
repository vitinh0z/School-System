package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "teachers")
public class Teacher {

    @Id
    private Integer id;

    @NotBlank(message = "Nome do professor é obrigatorio")
    @Column(nullable = false)
    private String name;
}
