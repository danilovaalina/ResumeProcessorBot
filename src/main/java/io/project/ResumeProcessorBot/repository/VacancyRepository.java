package io.project.ResumeProcessorBot.repository;


import io.project.ResumeProcessorBot.entity.Vacancy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VacancyRepository extends CrudRepository<Vacancy, Long> {
    @Query("select new Vacancy(v.id, v.name, v.requirement) from Vacancy v where fts(:name) = true")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    List<Vacancy> searchByName(String name);
}