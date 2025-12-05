package io.github.vitinh0z.schoolsystem.repository;

import io.github.vitinh0z.schoolsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContaining(String name);

    List<Student> findByClassId(Integer classId);

    Integer id(Integer id);

    List<Student> id(Integer id);

    List<Student> id(Integer id);
}
