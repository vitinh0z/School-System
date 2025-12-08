package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.Matter;
import io.github.vitinh0z.schoolsystem.repository.MatterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("matters")
@RequiredArgsConstructor

public class MatterController {

    private final MatterRepository matterRepository;


    @PostMapping("/create")
    public ResponseEntity<Matter> createMatter(@Valid @RequestBody Matter matter){

        Matter newMatter = matterRepository.save(matter);

        return ResponseEntity.ok(newMatter);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Matter>> allMatters(){

        List<Matter> matters = matterRepository.findAll();

        if (matters.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(matters);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<Matter>> deleteMatter (@PathVariable("id") Integer id){

        if (!matterRepository.existsById(id)){

            return ResponseEntity.notFound().build();
        }

        matterRepository.deleteById(id);

        List<Matter> findAll = matterRepository.findAll();

        return ResponseEntity.ok(findAll);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Matter> updateMatter (@PathVariable("id") Integer id,
                                                @Valid @RequestBody Matter matterJson
    ) {
        Optional<Matter> matterExistence = matterRepository.findById(id);

        if (matterExistence.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Matter matter = matterExistence.get();

        matter.setName(matterJson.getName());

        matterRepository.save(matter);

        return ResponseEntity.ok(matter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matter> getMatterById(@PathVariable("id") Integer id){
        return matterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()
                );
    }
}
