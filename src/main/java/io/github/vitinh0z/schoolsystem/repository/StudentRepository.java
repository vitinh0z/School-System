package io.github.vitinh0z.schoolsystem.repository;

import io.github.vitinh0z.schoolsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findStudentByDegreeId(Student student);

    List<Student> findStudentByNameContaining(Student student);

}
