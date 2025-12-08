package io.github.vitinh0z.schoolsystem.repository;

import io.github.vitinh0z.schoolsystem.model.SchoolSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<SchoolSchedule, Integer> {


}
