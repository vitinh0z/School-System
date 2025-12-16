package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.Degree;
import io.github.vitinh0z.schoolsystem.repository.DegreeRepository;
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
@RequestMapping("/degrees")
@RequiredArgsConstructor
@Tag(
        name = "Degree Controller",
        description = "API responsável pelo gerenciamento de graus/níveis acadêmicos"
)
public class DegreeController {

    private final DegreeRepository degreeRepository;

    @Operation(
            summary = "Cria um novo grau",
            description = "Cria e persiste um novo grau acadêmico no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grau criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<Degree> createDegree( @RequestBody Degree degree){
        Degree newDegree = degreeRepository.save(degree);
        return ResponseEntity.ok(newDegree);
    }

    @Operation(
            summary = "Lista todos os graus",
            description = "Retorna todos os graus cadastrados no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum grau encontrado")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Degree>> allDegrees(){

        List<Degree> degrees = degreeRepository.findAll();

        if (degrees.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(degrees);
    }

    @Operation(
            summary = "Remove um grau",
            description = "Exclui um grau acadêmico com base no ID informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grau removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Grau não encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<Degree>> deleteDegree(
            @Parameter(description = "ID do grau", example = "1")
            @PathVariable("id") Integer id
    ){
        if (!degreeRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        degreeRepository.deleteById(id);

        List<Degree> findAll = degreeRepository.findAll();

        return ResponseEntity.ok(findAll);
    }

    @Operation(
            summary = "Atualiza o nome de um grau",
            description = "Atualiza o nome de um grau acadêmico existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grau atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Grau não encontrado")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Degree> updateDegree(
            @Parameter(description = "ID do grau", example = "1")
            @PathVariable("id") Integer id,
            @Parameter(description = "Novo nome do grau", example = "Ensino Médio")
            @RequestParam("name") String name
    ){
        Optional<Degree> degreeExistence = degreeRepository.findById(id);

        if (degreeExistence.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Degree degree = degreeExistence.get();

        degree.setName(name);
        degreeRepository.save(degree);

        return ResponseEntity.ok(degree);
    }

    @Operation(
            summary = "Busca grau por ID",
            description = "Retorna os dados de um grau acadêmico específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grau encontrado"),
            @ApiResponse(responseCode = "404", description = "Grau não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Degree> getDegreeById(
            @Parameter(description = "ID do grau", example = "1")
            @PathVariable("id") Integer id
    ){
        return degreeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
