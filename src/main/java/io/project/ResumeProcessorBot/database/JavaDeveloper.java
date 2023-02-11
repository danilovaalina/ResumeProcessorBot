package io.project.ResumeProcessorBot.database;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "java_developer")
@TypeDef(
        name = "string-list",
        typeClass = ListArrayType.class
)
public class JavaDeveloper {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "position")
    private String position;

    @Type(type = "string-list")
    @Column(
            name = "technology_stack",
            columnDefinition = "text[]"
    )
    private List<String> technologyStack;

}
