package io.github.vitinh0z.schoolsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "school_schedule")
public class SchoolSchedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "matter_id")
    private Matter matter;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;


}
