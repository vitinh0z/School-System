package io.github.vitinh0z.schoolsystem.controller;


import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.model.Student;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.github.vitinh0z.schoolsystem.repository.StudentRepository;
import jakarta.persistence.Id;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentRepository studentRepository;


    @PostMapping
    public Student createStudent (@RequestBody Student student){

        return studentRepository.save(student);

    }



    @GetMapping
    public List<Student> getAllStudents() {

        return studentRepository.findAll();

    }   

    @PutMapping
    public String updateStudent(@RequestParam("id") Integer id,
                                @RequestParam("name") String name,
                                @RequestParam("classId") Integer classId
    ){

        Student findStudent = studentRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Estudante não Encotrado")
        );

        findStudent.setName(name);
        findStudent.setClassId(classId);
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


    public void getDashboardStatus(){
     // realize a dashboard with number of students
     //number ofclasses and number of degrees
    }





}
