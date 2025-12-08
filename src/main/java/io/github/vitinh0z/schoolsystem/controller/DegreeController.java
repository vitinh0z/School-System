package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.Degree;
import io.github.vitinh0z.schoolsystem.repository.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/degree")
@RequiredArgsConstructor
public class DegreeController {


    private final DegreeRepository degreeRepository;


    @PostMapping("/create")
    public ResponseEntity<Degree> createDegree(@RequestBody Degree degree){

        Degree newDegree = degreeRepository.save(degree);

        return ResponseEntity.ok(newDegree);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Degree>> allDegrees(){

        List<Degree> degrees = degreeRepository.findAll();

        if (degrees.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(degrees);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<Degree>> deleteDegree (@PathVariable("id") Integer id){

        if (!degreeRepository.existsById(id)){

            return ResponseEntity.notFound().build();
        }

        degreeRepository.deleteById(id);

        List<Degree> findAll = degreeRepository.findAll();

        return ResponseEntity.ok(findAll);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Degree> updateDegree (@PathVariable("id") Integer id,
                                                @RequestParam("name") String name
    ) {
        Optional<Degree> degreeExistence = degreeRepository.findById(id);

        if (degreeExistence.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Degree degree = degreeExistence.get();

        degree.setName(name);
        degreeRepository.save(degree);

        return ResponseEntity.ok(degree);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Degree> getDegreeById(@PathVariable("id") Integer id){
        return degreeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()
        );
    }
}
