package io.project.ResumeProcessorBot.database;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "technology_stack")
public class TechnologyStack {
    @Id
    @Column(name = "technology_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long technology_id;

    @Column(name = "technology")
    private String technology;

    @ManyToMany(mappedBy = "technologyStack")
    private List<JavaDeveloper> positions = new ArrayList<>();
}
