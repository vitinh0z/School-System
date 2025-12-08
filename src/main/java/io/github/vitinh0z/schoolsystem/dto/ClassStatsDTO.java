package io.github.vitinh0z.schoolsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassStatsDTO {

    private String className;
    private Long studentCount;

}
