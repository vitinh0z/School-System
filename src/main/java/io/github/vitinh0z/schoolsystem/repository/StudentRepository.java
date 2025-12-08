package io.github.vitinh0z.schoolsystem.repository;

import io.github.vitinh0z.schoolsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContaining(String name);

    List<Student> findByClassId(Integer classId);

    @Query("SELECT c.degree.name, COUNT(s) FROM Student s JOIN s.classEntity c GROUP BY c.degree.name")
    List<Object[]> countStudentByDegree();

    Long countByClassId (Integer classId);

}
