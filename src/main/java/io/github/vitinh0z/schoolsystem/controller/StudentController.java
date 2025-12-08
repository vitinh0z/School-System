package io.github.vitinh0z.schoolsystem.controller;


import io.github.vitinh0z.schoolsystem.dto.ClassStatsDTO;
import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.model.Student;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.github.vitinh0z.schoolsystem.repository.StudentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;


    @PostMapping
    public Student createStudent (@Valid @RequestBody Student student){

        return studentRepository.save(student);
    }

    @PostMapping("/generate")
    public List<Student> generateStudents (){

        Random random = new Random();
        List<Student> students = new ArrayList<>();
        List<ClassEntity> allClasses = classRepository.findAll();

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
        return studentRepository.saveAll(students);
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }   

    @PutMapping("/update/{id}")
    public String updateStudent(@Valid @RequestParam("id") Integer id,
                                    @RequestBody Student student){

        Student findStudent = studentRepository.findById(student.getId()).orElseThrow(()
                -> new RuntimeException("Estudante não Encotrado")
        );

        findStudent.setName(student.getName());
        findStudent.setClassId(student.getClassId());
        studentRepository.save(findStudent);

        return "/updateStudent=sucess";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") Integer id){

        Student findStudent = studentRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Estudante não Encotrado")
        );

        studentRepository.deleteById(id);
        return "/deleteStudent=sucess";
    }

    @GetMapping("dashboard/degree")
    public List<ClassStatsDTO> getDashboardStatus(){

        List<Object[]> allStudents = studentRepository.countStudentByDegree();

        List<ClassStatsDTO> classList = new ArrayList<>();

        for (Object[] student : allStudents){

            ClassStatsDTO dto = new ClassStatsDTO();
            dto.setClassName((String) student[0]);
            dto.setStudentCount((long) student[1]);

            classList.add(dto);
        }

        return classList;

    }
}
