package io.project.ResumeProcessorBot.ut.repository;

import io.project.ResumeProcessorBot.entity.Vacancy;
import io.project.ResumeProcessorBot.repository.VacancyRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VacancyRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    VacancyRepository vacancyRepository;

    @Test
    void Should_FindNoVacancies_IfRepositoryIsEmpty() {
        Iterable<Vacancy> vacancies = vacancyRepository.findAll();

        Assertions.assertThat(vacancies).isEmpty();
    }

    @Test
    void Should_SaveVacancy_ForValidVacancy() {
        Vacancy actual = new Vacancy(10L, "Java Junior", "Знание Java Collection, Java Multithreading. " +
                "Владение Spring Core, Spring MVC. Базовые знания в SQL");

        Vacancy expected = vacancyRepository.save(actual);

        Assertions.assertThat(expected).hasFieldOrPropertyWithValue("name", "Java Junior");
        Assertions.assertThat(expected).hasFieldOrPropertyWithValue("requirement", "Знание Java Collection, Java Multithreading. " +
                "Владение Spring Core, Spring MVC. Базовые знания в SQL");
    }

    @Test
    void Should_FindAllVacancies_IfVacanciesSaved() {
        Vacancy vacancy1 = new Vacancy( "Java Junior", "Знание Java Collection, Java Multithreading. " +
                "Владение Spring Core, Spring MVC. Базовые знания в SQL");
        entityManager.persist(vacancy1);

        Vacancy vacancy2 = new Vacancy( "Java Middle", "Java 11+ (core, multithreading). Spring (basic modules, spring-boot). " +
                "Kafka. Git, Maven. Опыт работы с реляционными СУБД PostgreSQL, Oracle");
        entityManager.persist(vacancy2);

        Vacancy vacancy3 = new Vacancy( "Java Senior", "Опыт разработки на Java 8+: JDBC, Spring & Spring Boot, Hibernate, Liquibase. " +
                "Понимание паттернов разработки. Сборка Java-проектов: Gradle.");
        entityManager.persist(vacancy3);

        Iterable<Vacancy> actualVacancies = vacancyRepository.findAll();
        Assertions.assertThat(actualVacancies).hasSize(3).contains(vacancy1, vacancy2, vacancy3);
    }

    @Test
    void Should_FindVacancies_ById() {
      Vacancy vacancy1 = new Vacancy( "Java Junior", "Знание Java Collection, Java Multithreading. " +
              "Владение Spring Core, Spring MVC. Базовые знания в SQL");
      entityManager.persist(vacancy1);

      Vacancy vacancy2 = new Vacancy( "Java Middle", "Java 11+ (core, multithreading). Spring (basic modules, spring-boot). " +
              "Kafka. Git, Maven. Опыт работы с реляционными СУБД PostgreSQL, Oracle");
      entityManager.persist(vacancy2);

      Optional<Vacancy> foundVacancy = vacancyRepository.findById(vacancy2.getId());

      foundVacancy.ifPresent(vacancy -> Assertions.assertThat(vacancy).isEqualTo(vacancy2));
    }

    @Test
    void Should_UpdateVacancy_ById() {
        Vacancy vacancy1 = new Vacancy( "Java Junior", "Знание Java Collection, Java Multithreading. " +
                "Владение Spring Core, Spring MVC. Базовые знания в SQL");
        entityManager.persist(vacancy1);

        Vacancy vacancy2 = new Vacancy( "Java Middle", "Java 11+ (core, multithreading). Spring (basic modules, spring-boot). " +
                "Kafka. Git, Maven. Опыт работы с реляционными СУБД PostgreSQL, Oracle");
        entityManager.persist(vacancy2);

        Vacancy updatedVacancy = new Vacancy("Java Senior", "Проектирование и разработка модулей back-end приложения. " +
                "Покрытие тестами разработанного функционала. Проведение code review коллег по команде. Декомпозиция и оценка");

        Vacancy vacancy = vacancyRepository.findById(vacancy2.getId()).get();
        vacancy.setName(updatedVacancy.getName());
        vacancy.setRequirement(updatedVacancy.getRequirement());
        vacancyRepository.save(vacancy);

        Vacancy checkVacancy = vacancyRepository.findById(vacancy2.getId()).get();

        Assertions.assertThat(checkVacancy.getId()).isEqualTo(vacancy2.getId());
        Assertions.assertThat(checkVacancy.getName()).isEqualTo(updatedVacancy.getName());
        Assertions.assertThat(checkVacancy.getRequirement()).isEqualTo(updatedVacancy.getRequirement());
    }

    @Test
    void Should_DeleteVacancy_ById() {
        Vacancy vacancy1 = new Vacancy( "Java Junior", "Знание Java Collection, Java Multithreading. " +
                "Владение Spring Core, Spring MVC. Базовые знания в SQL");
        entityManager.persist(vacancy1);

        Vacancy vacancy2 = new Vacancy( "Java Middle", "Java 11+ (core, multithreading). Spring (basic modules, spring-boot). " +
                "Kafka. Git, Maven. Опыт работы с реляционными СУБД PostgreSQL, Oracle");
        entityManager.persist(vacancy2);

        vacancyRepository.deleteById(vacancy1.getId());

        Iterable<Vacancy> vacancies = vacancyRepository.findAll();

        Assertions.assertThat(vacancies).hasSize(1).contains(vacancy2);
        Assertions.assertThat(vacancyRepository.findById(vacancy1.getId())).isNotPresent();
    }

    @Test
    void Should_DeleteAlLVacancies() {
        
        Vacancy vacancy1 = new Vacancy( "Java Junior", "Знание Java Collection, Java Multithreading. " +
                "Владение Spring Core, Spring MVC. Базовые знания в SQL");
        entityManager.persist(vacancy1);

        Vacancy vacancy2 = new Vacancy( "Java Middle", "Java 11+ (core, multithreading). Spring (basic modules, spring-boot). " +
                "Kafka. Git, Maven. Опыт работы с реляционными СУБД PostgreSQL, Oracle");
        entityManager.persist(vacancy2);

    }



}