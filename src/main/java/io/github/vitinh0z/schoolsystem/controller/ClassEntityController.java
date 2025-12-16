package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("class")
@RestController
@RequiredArgsConstructor
@Tag(name = "Class Controller", description = "API responsible management of the class")
public class ClassEntityController {

    private final ClassRepository classRepository;

    @Operation(
            summary = "Cria uma nova sala",
            description = "Cria e persiste uma nova entidade de sala no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<ClassEntity> createClassEntity(
            @Valid @RequestBody ClassEntity classEntity
    ){
        return ResponseEntity.ok(classRepository.save(classEntity));
    }

    @Operation(
            summary = "Lista todas as salas",
            description = "Retorna todas as salas cadastradas no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<ClassEntity>> allClassEntity (){
        return ResponseEntity.ok(classRepository.findAll());
    }

    @Operation(
            summary = "Atualiza uma sala existente",
            description = "Atualiza os dados de uma sala com base no ID informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<ClassEntity> updateClassEntity(
            @Parameter(description = "ID da sala", example = "1")
            @PathVariable("id") Integer id,
            @Valid @RequestBody ClassEntity classEntityjson
    ){
        Optional<ClassEntity> findClassEntity = classRepository.findById(id);

        if (findClassEntity.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        ClassEntity classEntityBanco = findClassEntity.get();
        classEntityBanco.setName(classEntityjson.getName());
        classRepository.save(classEntityBanco);

        if(classEntityjson.getDegreeId() != null){
            classEntityBanco.setDegreeId(classEntityjson.getDegreeId());
        }
        return ResponseEntity.ok(classEntityBanco);
    }

    @Operation(
            summary = "Remove uma sala",
            description = "Exclui uma sala existente com base no ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClassEntity> deleteClassEntity(
            @Parameter(description = "ID da sala", example = "1")
            @PathVariable("id") Integer id
    ){
        Optional<ClassEntity> findClassEntity = classRepository.findById(id);

        if (findClassEntity.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        ClassEntity classEntity = findClassEntity.get();
        classRepository.deleteById(id);
        return ResponseEntity.ok(classEntity);
    }

    @Operation(
            summary = "Busca sala por ID",
            description = "Retorna os dados de uma sala específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala encontrada"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClassEntity> getClassEntityById(
            @Parameter(description = "ID da sala", example = "1")
            @PathVariable("id") Integer id
    ){
        return classRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()
        );
    }
}
