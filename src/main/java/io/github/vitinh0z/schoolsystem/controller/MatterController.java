package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.Matter;
import io.github.vitinh0z.schoolsystem.repository.MatterRepository;
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
@RequestMapping("matters")
@RequiredArgsConstructor
@Tag(
        name = "Matter Controller",
        description = "API responsável pelo gerenciamento de matérias"
)
public class MatterController {

    private final MatterRepository matterRepository;

    @Operation(
            summary = "Cria uma nova matéria",
            description = "Cria e persiste uma nova matéria no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matéria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<Matter> createMatter(
            @Valid @RequestBody Matter matter
    ){
        Matter newMatter = matterRepository.save(matter);
        return ResponseEntity.ok(newMatter);
    }

    @Operation(
            summary = "Lista todas as matérias",
            description = "Retorna todas as matérias cadastradas no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhuma matéria encontrada")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Matter>> allMatters(){

        List<Matter> matters = matterRepository.findAll();

        if (matters.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(matters);
    }

    @Operation(
            summary = "Remove uma matéria",
            description = "Exclui uma matéria existente com base no ID informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matéria removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Matéria não encontrada")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<Matter>> deleteMatter(
            @Parameter(description = "ID da matéria", example = "1")
            @PathVariable("id") Integer id
    ){
        if (!matterRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        matterRepository.deleteById(id);

        List<Matter> findAll = matterRepository.findAll();

        return ResponseEntity.ok(findAll);
    }

    @Operation(
            summary = "Atualiza uma matéria",
            description = "Atualiza os dados de uma matéria existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matéria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Matéria não encontrada")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Matter> updateMatter(
            @Parameter(description = "ID da matéria", example = "1")
            @PathVariable("id") Integer id,
            @Valid @RequestBody Matter matterJson
    ){
        Optional<Matter> matterExistence = matterRepository.findById(id);

        if (matterExistence.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Matter matter = matterExistence.get();

        matter.setName(matterJson.getName());

        matterRepository.save(matter);

        return ResponseEntity.ok(matter);
    }

    @Operation(
            summary = "Busca matéria por ID",
            description = "Retorna os dados de uma matéria específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matéria encontrada"),
            @ApiResponse(responseCode = "404", description = "Matéria não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Matter> getMatterById(@Parameter(description = "ID da matéria", example = "1")
                                                    @PathVariable("id") Integer id
    ){
        return matterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
