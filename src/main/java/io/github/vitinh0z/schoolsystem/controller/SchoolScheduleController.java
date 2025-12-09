package io.github.vitinh0z.schoolsystem.controller;

import io.github.vitinh0z.schoolsystem.model.SchoolSchedule;
import io.github.vitinh0z.schoolsystem.repository.SchoolSchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class SchoolScheduleController {

    private final SchoolSchedulerRepository schoolSchedulerRepository;

    // Create
    @PostMapping("/create")
    public ResponseEntity<SchoolSchedule> createSchedule (@RequestBody SchoolSchedule schoolSchedule){
        return ResponseEntity.ok(schoolSchedulerRepository.save(schoolSchedule));
    }

    //Reload
    @GetMapping("/all")
    public ResponseEntity<List<SchoolSchedule>> allSchedule (){
        return ResponseEntity.ok(schoolSchedulerRepository.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SchoolSchedule> updateSchedule (@PathVariable("id") Integer id,
                                                          @RequestBody SchoolSchedule schoolScheduleJson)
    {
        Optional<SchoolSchedule> findByIdSchedule = schoolSchedulerRepository.findById(id);

        if (findByIdSchedule.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        SchoolSchedule schoolSchedule = findByIdSchedule.get();

        schoolSchedule.setClassEntity(schoolScheduleJson.getClassEntity());
        schoolSchedule.setMatter(schoolScheduleJson.getMatter());
        schoolSchedule.setTeacher(schoolScheduleJson.getTeacher());

        return ResponseEntity.ok(schoolSchedulerRepository.save(schoolSchedule));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SchoolSchedule> deleteByIdSchedule (@PathVariable("id") Integer id){

        Optional<SchoolSchedule> findByIdSchedule = schoolSchedulerRepository.findById(id);

        if (findByIdSchedule.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        SchoolSchedule schoolSchedule = findByIdSchedule.get();

        schoolSchedulerRepository.deleteById(id);

        return ResponseEntity.ok(schoolSchedule);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolSchedule> getScheduleById (@PathVariable("id") Integer id){
        return schoolSchedulerRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()
        );
    }
}
