package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.Teacher;
import io.github.vitinh0z.schoolsystem.repository.TeacherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherRepository teacherRepository;

    @PostMapping("/create")
    public ResponseEntity<Teacher> createTeacher (@Valid @RequestBody Teacher teacher){
        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Teacher>> allTeacher (){
        return ResponseEntity.ok(teacherRepository.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") Integer id,
                                                @Valid @RequestBody Teacher teacherjson)
    {
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Teacher> deleteTeacher (@PathVariable("id") Integer id){

        Optional<Teacher> findTeacher = teacherRepository.findById(id);

        if (findTeacher.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Teacher teacher = findTeacher.get();

        teacherRepository.deleteById(id);

        return ResponseEntity.ok(teacher);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById (@PathVariable("id") Integer id){
        return teacherRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



}
