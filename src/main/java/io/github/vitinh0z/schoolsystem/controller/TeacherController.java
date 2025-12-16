package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.Teacher;
import io.github.vitinh0z.schoolsystem.repository.TeacherRepository;
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

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
@Tag(
        name = "Teacher Controller",
        description = "API responsável pelo gerenciamento de professores"
)
public class TeacherController {

    private final TeacherRepository teacherRepository;

    @Operation(
            summary = "Cria um professor",
            description = "Cria e persiste um novo professor no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<Teacher> createTeacher (
            @Valid @RequestBody Teacher teacher
    ){
        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    @Operation(
            summary = "Lista todos os professores",
            description = "Retorna todos os professores cadastrados no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<Teacher>> allTeacher (){
        return ResponseEntity.ok(teacherRepository.findAll());
    }

    @Operation(
            summary = "Atualiza um professor",
            description = "Atualiza os dados de um professor existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Teacher> updateTeacher(
            @Parameter(description = "ID do professor", example = "1")
            @PathVariable("id") Integer id,
            @Valid @RequestBody Teacher teacherjson
    ){
        Optional<Teacher> findTeacher = teacherRepository.findById(id);

        if (findTeacher.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Teacher teacherBanco = findTeacher.get();

        teacherBanco.setName(teacherjson.getName());
        teacherBanco.setSubject(teacherjson.getSubject());
        teacherRepository.save(teacherBanco);

        return ResponseEntity.ok(teacherBanco);
    }

    @Operation(
            summary = "Remove um professor",
            description = "Exclui um professor com base no ID informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Teacher> deleteTeacher (
            @Parameter(description = "ID do professor", example = "1")
            @PathVariable("id") Integer id
    ){
        Optional<Teacher> findTeacher = teacherRepository.findById(id);

        if (findTeacher.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Teacher teacher = findTeacher.get();

        teacherRepository.deleteById(id);

        return ResponseEntity.ok(teacher);
    }

    @Operation(
            summary = "Busca professor por ID",
            description = "Retorna os dados de um professor específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor encontrado"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById (
            @Parameter(description = "ID do professor", example = "1")
            @PathVariable("id") Integer id
    ){
        return teacherRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()
        );
    }
}
