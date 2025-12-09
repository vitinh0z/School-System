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
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final DegreeRepository degreeRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;
    private final SchoolSchedulerRepository schoolSchedulerRepository; // Supondo que este repo gerencia SchoolSchedule
    private final TeacherRepository teacherRepository;
    private final MatterRepository matterRepository;


    @Override
    public void run(String... args) throws Exception { // método responsável por criar base de dados
        // assim que o spring iniciar

        log.info("Criando base de dados...");

        // Limpeza opcional para evitar duplicidade ao reiniciar
        schoolSchedulerRepository.deleteAll();
        studentRepository.deleteAll();
        classRepository.deleteAll();
        teacherRepository.deleteAll();
        matterRepository.deleteAll();
        degreeRepository.deleteAll();

        loadDegree();
        loadClass();
        loadsTeacher();
        loadMatters();
        loadsStudent();

        loadRelationShip();
        log.info("Base de dados criada com sucesso!");
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
            List<SchoolSchedule> relationshipsEntities = new ArrayList<>();

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

                        SchoolSchedule schoolSchedule = new SchoolSchedule();


                        if (teachersDto.getTeacherId() != null) {
                            schoolSchedule.setTeacher(teacherRepository.getReferenceById
                                    (teachersDto.getTeacherId())
                            );
                        }

                        if (teachersDto.getMatterId() != null) {
                            schoolSchedule.setMatter(matterRepository.getReferenceById
                                    (teachersDto.getMatterId())
                            );
                        }

                        // Lógica da turma mantida, mas adaptada para pegar referencia

                        // Não sei se foi de propósito, mas no json tem vezes que manda classId
                        // e classPosition. Para isso, precisamos verificar se
                        // um estiver nulo, pegaremos o outro.
                        // ------------------------------------------------------------------------


                        /* (isso se chama Operador Ternário. É a mesma coisa de fazer:)

                         * integer idTurma =

                         * if (idTurma!= null){

                         * idTurma = classdto.getClassId()

                         * }
                         *
                         * else {
                         * idTurma = classesDto.getClassPosition()
                         * }
                         *
                         * */

                        Integer idTurma = classesDto.getClassId() != null ? classesDto.getClassId()
                                : classesDto.getClassPosition();

                        if (idTurma != null) {
                            ClassEntity classRef = classRepository.getReferenceById(idTurma);
                            schoolSchedule.setClassEntity(classRef);
                        }

                        // adicionamos ele na lista
                        relationshipsEntities.add(schoolSchedule);

                    }
                }
            }
            schoolSchedulerRepository.saveAll(relationshipsEntities);

        } catch (Exception e) {
            log.error("Erro ao carregar relacionamentos: {}", e.getMessage());
            e.printStackTrace(); // Ajuda a ver o erro real
        }
    }

    public void loadMatters(){
        try (InputStream inputStream = new ClassPathResource("data/matters.json").getInputStream()){

            List<Matter> matters = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {}
            );

            // limpar ID para evitar erro
            matters.forEach(m -> m.setId(null));

            matterRepository.saveAll(matters);

        } catch (IOException e) {
            log.error("Erro Matters: {}", e.getMessage());
        }
    }

    public void loadClass() {

        Random random = new Random();
        List<Degree> allDegrees = degreeRepository.findAll();

        try (InputStream inputStream = new ClassPathResource("data/classes.json").getInputStream()) {
            List<ClasseDTO> classeDTOS = this.objectMapper.
                    readValue(inputStream, ClassWrapper.class).getClasses();

            List<ClassEntity> classEntities = new ArrayList<>();


            for (int i =0; i< allDegrees.size();i++) {

                Degree degree = allDegrees.get(i);

                ClassEntity classEntity = new ClassEntity();
                classEntity.setId(null);

                classEntity.setDegreeId(degree.getId());

                ClasseDTO nomeDto = classeDTOS.get(i % classeDTOS.size());
                classEntity.setName(nomeDto.getName());

                classEntities.add(classEntity);

            }

            classRepository.saveAll(classEntities);

        } catch (IOException e) {
            log.error("Erro Class: {}", e.getMessage());
        }
    }

    public void loadsTeacher (){

        try (InputStream inputStream = new ClassPathResource("data/teachers.json").getInputStream()){

            List<Teacher> teachers = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {}
            );

            // passar sobre a lista carregada, limpar ID e garantir Matéria
            teachers.forEach(teacher -> {
                teacher.setId(null);
                // Garante que tenha matéria para não dar erro de validação @NotBlank
                if(teacher.getSubject() == null || teacher.getSubject().isEmpty()) {
                    teacher.setSubject("Matéria Geral");
                }
            });

            teacherRepository.saveAll(teachers); // Salva a lista

        } catch (IOException e){
            log.error("Erro Teacher: {}", e.getMessage());
        }
    }

    public void loadsStudent (){

        try(InputStream inputStream = new ClassPathResource("data/students.json").getInputStream()) {

            List<Student> students = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {}
            );

            students.forEach(student -> student.setId(null));

            studentRepository.saveAll(students);

        } catch (FileNotFoundException e){
            log.error("Arquivo não encotrado: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Erro Student: {}", e.getMessage());
        }
    }

    public void loadDegree (){
        try (InputStream inputStream = new ClassPathResource("data/degrees.json").getInputStream()){

            List<Degree> degrees = this.objectMapper.readValue(inputStream,
                    new TypeReference<>() {}
            );

            // Limpar IDs
            degrees.forEach(d -> d.setId(null));

            degreeRepository.saveAll(degrees);

        } catch (FileNotFoundException e){
            log.error("Arquivo não encotrado {}", e.getMessage());

        } catch(IOException e){
            log.error("Erro Degree: {}", e.getMessage());
        }

    }
}