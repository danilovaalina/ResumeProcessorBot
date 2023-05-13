package io.project.ResumeProcessorBot.entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Table(name = "vacancies")
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "schedule")
    private String schedule;

    @Column(name = "area")
    private String area;

    @Column(name = "requirement")
    private String requirement;

    public Vacancy(String name, String requirement) {
        this.name = name;
        this.requirement =  requirement;
    }

    public Vacancy(Long id, String name, String requirement) {
        this.id = id;
        this.name = name;
        this.requirement = requirement;
    }
}
