package io.github.vitinh0z.schoolsystem.repository;

import io.github.vitinh0z.schoolsystem.model.ClassEntity;
import io.github.vitinh0z.schoolsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    List<Student> findByDegreeId

}
