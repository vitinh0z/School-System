package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.dto.ClassStatsDTO;
import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.model.Student;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.github.vitinh0z.schoolsystem.repository.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(
        name = "Student Controller",
        description = "API responsável pelo gerenciamento de alunos e estatísticas relacionadas"
)
public class StudentController {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;

    @Operation(
            summary = "Cria um aluno",
            description = "Cria e persiste um novo aluno no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<Student> createStudent (
            @Valid @RequestBody Student student
    ){
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @Operation(
            summary = "Gera alunos automaticamente",
            description = "Gera uma lista de alunos fictícios distribuídos aleatoriamente entre as turmas existentes"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alunos gerados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não existem turmas cadastradas")
    })
    @PostMapping("/generate")
    public ResponseEntity<List<Student>> generateStudents (){

        Random random = new Random();
        List<Student> students = new ArrayList<>();
        List<ClassEntity> allClasses = classRepository.findAll();

        if (allClasses.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        int i = 0;
        while (i < 300){

            int index = random.nextInt(allClasses.size());

            ClassEntity randomClass = allClasses.get(index);

            Student student = new Student();
            student.setName("Aluno " + i);
            student.setClassId(randomClass.getId());

            students.add(student);

            i++;
        }

        students = studentRepository.saveAll(students);

        return ResponseEntity.ok(students);
    }

    @Operation(
            summary = "Lista todos os alunos",
            description = "Retorna todos os alunos cadastrados no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @Operation(
            summary = "Atualiza um aluno",
            description = "Atualiza os dados de um aluno existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable("id") Integer id,
            @Valid @RequestBody Student studentJson
    ){
        Optional<Student> find = studentRepository.findById(id);

        if (find.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Student stundentBanco = find.get();

        stundentBanco.setClassId(studentJson.getClassId());
        stundentBanco.setName(studentJson.getName());

        Student stundentSalvo = studentRepository.save(stundentBanco);

        return ResponseEntity.ok(stundentSalvo);
    }

    @Operation(
            summary = "Remove um aluno",
            description = "Exclui um aluno com base no ID informado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Student> deleteStudent(
            @Parameter(description = "ID do aluno", example = "1")
            @PathVariable("id") Integer id
    ){
        Optional<Student> findStudent = studentRepository.findById(id);

        if(findStudent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Student student = findStudent.get();

        studentRepository.deleteById(id);

        return ResponseEntity.ok(student);
    }

    @Operation(
            summary = "Dashboard de alunos por grau",
            description = "Retorna estatísticas com a quantidade de alunos agrupados por grau"
    )
    @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
    @GetMapping("dashboard/degree")
    public ResponseEntity<List<ClassStatsDTO>> getDashboardStatus(){

        List<Object[]> allStudents = studentRepository.countStudentByDegree();

        List<ClassStatsDTO> classList = new ArrayList<>();

        for (Object[] student : allStudents){

            ClassStatsDTO dto = new ClassStatsDTO();
            dto.setClassName((String) student[0]);
            dto.setStudentCount((long) student[1]);

            classList.add(dto);
        }
        return ResponseEntity.ok(classList);
    }
}
