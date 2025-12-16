package io.github.vitinh0z.schoolsystem.controller;


import io.github.vitinh0z.schoolsystem.dto.ClassStatsDTO;
import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.github.vitinh0z.schoolsystem.repository.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(
        name = "Dashboard Controller",
        description = "Endpoints responsáveis por estatísticas e dados agregados do sistema"
)
public class DashboardController {


    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    @Operation(
            summary = "Dashboard de alunos por turma",
            description = "Retorna uma lista com o nome de cada turma e a quantidade total de alunos matriculados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
    })
    @GetMapping("students/dashboard")
    public List<ClassStatsDTO> dashboard (){

        List<ClassEntity> allClass = classRepository.findAll();
        List<ClassStatsDTO> classStatsDTOList = new ArrayList<>();

        // Pega todas as classes [A, B, C, D, E, F]

        // passamos dentro da classe pegando cada id das class
        // [A, B, C, D, E]
        //  A --> seria ‘ID 1'
        //  B --> seria ‘ID 2'
        // e assim por diante

        for (ClassEntity turma : allClass){

            ClassStatsDTO dto = new ClassStatsDTO();
            //aqui iremos passar para o dto da classe qual o nome da turma

            dto.setClassName(turma.getName());
            // depois pegar o id dele e realizar a contagem da quantidade de alunos
            // em cada classe pelo id dela
            // Mandamos o ID da turma A (ex: 1) para o banco e ele responde
            // só o NÚMERO da quantidade(ex: 30).
            dto.setStudentCount(studentRepository.countByClassId(turma.getId()));

            // e adicionamos na lista
            classStatsDTOList.add(dto);
        }
        return classStatsDTOList;
    }
}
