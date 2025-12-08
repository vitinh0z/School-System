package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("class")
@RestController
@RequiredArgsConstructor
public class ClassEntityController {

    private final ClassRepository classRepository;


    @PostMapping("/create")
    public ResponseEntity<ClassEntity> createClassEntity (@Valid @RequestBody ClassEntity classEntity){
        return ResponseEntity.ok(classRepository.save(classEntity));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClassEntity>> allClassEntity (){
        return ResponseEntity.ok(classRepository.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClassEntity> updateClassEntity(@PathVariable("id") Integer id,
                                                 @Valid @RequestBody ClassEntity classEntityjson)
    {
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClassEntity> deleteClassEntity (@PathVariable("id") Integer id){

        Optional<ClassEntity> findClassEntity = classRepository.findById(id);

        if (findClassEntity.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        ClassEntity classEntity = findClassEntity.get();

        classRepository.deleteById(id);

        return ResponseEntity.ok(classEntity);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClassEntity> getClassEntityById (@PathVariable("id") Integer id){
        return classRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
