package io.github.vitinh0z.schoolsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vitinh0z.schoolsystem.model.*;
import io.github.vitinh0z.schoolsystem.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final DegreeRepository degreeRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;
    private final RelationshipRepository relationshipRepository;
    private final TeacherRepository teacherRepository;
    private final MatterRepository matterRepository;

    @Override
    public void run(String... args) throws Exception { // método responsável por criar base de dados
        // assim que o spring iniciar
        log.info("Criando base de dados");

        loadDegree();
        loadClass();
        loadsTeacher();
        loadMatters();
        loadsStudent();
        loadRelationShip();
    }

    @Data
    static class ClassWrapper{
        private List<ClasseDTO> classes;
    }

    @Data
    static class ClasseDTO{
        private String name;
    }

    @Data
    static class RelTeacherDTO{
        private Integer id;
        private Integer teacherId;
        private Integer matterId;
        private List<RelDegreeDTO> degrees = new ArrayList<>();
        // professor de uma lista de cursos que da aula
    }

    @Data
    static class RelClassDTO{
        // classe pode ter classPosition
        private Integer classPosition;
        // ou classId
        private Integer classId;
    }

    @Data
    static class RelDegreeDTO{
        // e curso tem um id
        private Integer degreeId;

        private List<RelClassDTO> classes = new ArrayList<>();
        // o curso tem uma lista de classes
    }

    public void loadRelationShip(){

        try (InputStream inputStream = new ClassPathResource("data/relationships.json").
                getInputStream()){

            List<RelTeacherDTO> relationshipsDTOS = this.objectMapper.
                    readValue(inputStream, new TypeReference<>() {});
            List<Relationship> relationshipsEntities = new ArrayList<>();

            /*
            *
            * passamos dentro da classe professores e pegamos os 'cursos'
            * depois pegar qual sala do curso o professor da aulas
            *
            * Objetivo é achar o json para ele ficar: Professor + curso + sala
            * */

            for (RelTeacherDTO teachersDto : relationshipsDTOS){
                for (RelDegreeDTO degreesDto : teachersDto.getDegrees()){
                    for (RelClassDTO classesDto : degreesDto.getClasses()){

                        Relationship relationship = new Relationship();

                        Teacher teacher = new Teacher();

                        teacher.setId(teachersDto.getTeacherId());
                        relationship.setTeacher(teacher);

                        Matter matter = new Matter();
                        matter.setId(teachersDto.getMatterId());
                        relationship.setMatter(matter);

                        ClassEntity classEntity = new ClassEntity();

                        // nao sei se foi de propósito, mas no json tem vezes que manda classId
                        // e classPosition. Para isso, precisamos verificar se
                        // um estiver nulo, pegaremos o outro.
                        // ------------------------------------------------------------------------


                        /* (isso se chama Operador Ternário. É a mesma coisa de fazer:)

                        * integer idTurma =

                        * if (idTurma!= null){

                        *   idTurma = classdto.getClassId()

                        * }
                        *
                        * else {
                        *   idTurma = classesDto.getClassPosition()
                        * }
                        *
                        * */

                        Integer idTurma = classesDto.getClassId() != null ? classesDto.getClassId()
                                : classesDto.getClassPosition();
                        classEntity.setId(idTurma);
                        relationship.setClassEntity(classEntity);


                        // adicionamos ele na lista
                        relationshipsEntities.add(relationship);

                    }
                }
            }
            relationshipRepository.saveAll(relationshipsEntities);

        } catch (Exception e) {
            log.error("Erro: {}", e.getMessage());
        }
    }

    public void loadMatters(){
        try (InputStream inputStream = new ClassPathResource("data/matters.json").getInputStream()){

            List<Matter> matters = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {
                    }
            );

            matterRepository.saveAll(matters);

        } catch (IOException e) {
            log.error("Erro: {}", e.getMessage());
        }
    }

    public void loadClass() {

        try (InputStream inputStream = new ClassPathResource("data/classes.json").getInputStream()) {
            List<ClasseDTO> classeDTOS = this.objectMapper.
                    readValue(inputStream, ClassWrapper.class).getClasses();

            List<ClassEntity> classEntities = new ArrayList<>();

            int id = 1;
            for (ClasseDTO item : classeDTOS) {

                ClassEntity classEntity = new ClassEntity();
                classEntity.setId(id++);
                classEntity.setName(item.getName());
                classEntity.setDegreeId(1);
                classEntities.add(classEntity);
            }

            classRepository.saveAll(classEntities);

        } catch (IOException e) {
            log.error("erro: {}", e.getMessage());
        }
    }

    public void loadsTeacher (){

        try (InputStream inputStream = new ClassPathResource("data/teachers.json").getInputStream()){

            List<Teacher> teachers = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {}
            );

            teacherRepository.saveAll(teachers);

        } catch (IOException e){
            log.error("Erro: {}", e.getMessage());
        }
    }

    public void loadsStudent (){

        try(InputStream inputStream = new ClassPathResource("data/students.json").getInputStream()) {

            List<Student> students = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {}
            );

            studentRepository.saveAll(students);

        } catch (FileNotFoundException e){
            log.error("Arquivo não encotrado: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Erro: {}", e.getMessage());
        }
    }

    public void loadDegree (){
        try (InputStream inputStream = new ClassPathResource("data/degrees.json").getInputStream()){

            List<Degree> degrees = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {}
            );
            degreeRepository.saveAll(degrees);

        } catch (FileNotFoundException e){
            log.error("Arquivo não encotrado {}", e.getMessage());

        } catch(IOException e){
            log.error("Erro {}", e.getMessage());
        }

    }
}
