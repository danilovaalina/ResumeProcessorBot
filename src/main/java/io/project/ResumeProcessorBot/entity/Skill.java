package io.project.ResumeProcessorBot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long technology_id;

    @Column(name = "skill")
    private String skill;

    @ManyToMany(mappedBy = "skills")
    private List<Specialist> specialists = new ArrayList<>();
}
