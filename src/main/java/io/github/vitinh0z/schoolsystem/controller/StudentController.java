package io.github.vitinh0z.schoolsystem.controller;


import io.github.vitinh0z.schoolsystem.model.Student;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.github.vitinh0z.schoolsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/students")
public class StudentController {

    @Autowired
    private ClassRepository classRepository;

    private StudentRepository studentRepository;



    private Student student;



    public void getAllStudents() {



        classRepository.findAll();

    }

    public String updateStudent(@RequestParam("id") String name) {

        return "/updateStudent=sucess";

    }


    public String deleteStudent(@RequestParam("id") Integer id){




        return "/deleteStudent=sucess";

    }

    public void getDashboardStatus(){
     // realize a dashboard with number of students
     //number ofclasses and number of degrees
    }





}
