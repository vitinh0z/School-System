package io.github.vitinh0z.schoolsystem.controller;


import io.github.vitinh0z.schoolsystem.dto.ClassStatsDTO;
import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.model.Student;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.github.vitinh0z.schoolsystem.repository.StudentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;


    @PostMapping("/create")
    public ResponseEntity<Student> createStudent (@Valid @RequestBody Student student){

        return ResponseEntity.ok(studentRepository.save(student));
    }

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

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {

        return ResponseEntity.ok(studentRepository.findAll());
    }   

    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent( @PathVariable("id") Integer id,
                                                  @Valid @RequestBody Student studentJson){

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") Integer id){

        Optional<Student> findStudent = studentRepository.findById(id);

        if(findStudent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Student student = findStudent.get();

        studentRepository.deleteById(id);

        return ResponseEntity.ok(student);
    }

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
