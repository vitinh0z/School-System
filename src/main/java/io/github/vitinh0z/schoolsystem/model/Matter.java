package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matters")
@Data
public class Matter {

    @Id
    private Integer id;

    @NotBlank(message = "É Obrigatorio Materia")
    @Column(nullable = false)
    private String name;

}
