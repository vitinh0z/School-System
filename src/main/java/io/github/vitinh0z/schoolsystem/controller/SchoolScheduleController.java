package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.SchoolSchedule;
import io.github.vitinh0z.schoolsystem.repository.SchoolSchedulerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Tag(
        name = "School Schedule Controller",
        description = "API responsável pelo gerenciamento de horários escolares"
)
public class SchoolScheduleController {

    private final SchoolSchedulerRepository schoolSchedulerRepository;

    // Create
    @Operation(
            summary = "Cria um horário escolar",
            description = "Cria e persiste um novo horário escolar no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<SchoolSchedule> createSchedule (
            @RequestBody SchoolSchedule schoolSchedule
    ){
        return ResponseEntity.ok(schoolSchedulerRepository.save(schoolSchedule));
    }

    //Reload
    @Operation(
            summary = "Lista todos os horários",
            description = "Retorna todos os horários escolares cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<SchoolSchedule>> allSchedule (){
        return ResponseEntity.ok(schoolSchedulerRepository.findAll());
    }

    @Operation(
            summary = "Atualiza um horário escolar",
            description = "Atualiza os dados de um horário escolar existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<SchoolSchedule> updateSchedule (
            @Parameter(description = "ID do horário", example = "1")
            @PathVariable("id") Integer id,
            @RequestBody SchoolSchedule schoolScheduleJson
    ){
        Optional<SchoolSchedule> findByIdSchedule = schoolSchedulerRepository.findById(id);

        if (findByIdSchedule.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        SchoolSchedule schoolSchedule = findByIdSchedule.get();

        schoolSchedule.setClassEntity(schoolScheduleJson.getClassEntity());
        schoolSchedule.setMatter(schoolScheduleJson.getMatter());
        schoolSchedule.setTeacher(schoolScheduleJson.getTeacher());

        return ResponseEntity.ok(schoolSchedulerRepository.save(schoolSchedule));
    }

    @Operation(
            summary = "Remove um horário escolar",
            description = "Exclui um horário escolar com base no ID informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SchoolSchedule> deleteByIdSchedule (
            @Parameter(description = "ID do horário", example = "1")
            @PathVariable("id") Integer id
    ){
        Optional<SchoolSchedule> findByIdSchedule = schoolSchedulerRepository.findById(id);

        if (findByIdSchedule.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        SchoolSchedule schoolSchedule = findByIdSchedule.get();

        schoolSchedulerRepository.deleteById(id);

        return ResponseEntity.ok(schoolSchedule);
    }

    @Operation(
            summary = "Busca horário por ID",
            description = "Retorna os dados de um horário escolar específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horário encontrado"),
            @ApiResponse(responseCode = "404", description = "Horário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SchoolSchedule> getScheduleById (
            @Parameter(description = "ID do horário", example = "1")
            @PathVariable("id") Integer id
    ){
        return schoolSchedulerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
