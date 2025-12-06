package io.github.vitinh0z.schoolsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vitinh0z.schoolsystem.model.Student;
import io.github.vitinh0z.schoolsystem.repository.ClassRepository;
import io.github.vitinh0z.schoolsystem.repository.DegreeRepository;
import io.github.vitinh0z.schoolsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final DegreeRepository degreeRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;



    @Override
    public void run(String... args) throws Exception {
        log.info("Criando base de dados");

        loadsStudent();




    }

    public List<Student> loadsStudent (){

        try {
            InputStream inputStream = new ClassPathResource("data/students.json").getInputStream();

            List<Student> students = this.objectMapper.readValue(inputStream,
                    new TypeReference<List<Student>>(){}
            );

            return studentRepository.saveAll(students);

        } catch (FileNotFoundException e){
            log.error("Arquivo não encotrado: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Erro: {}", e.getMessage());
        }

        return Collections.emptyList();
    }


    // ToDo Implementar a mesma logica do loadsStudent com os outros JSON


}
