package io.project.ResumeProcessorBot.ut.service;

import io.project.ResumeProcessorBot.entity.Vacancy;
import io.project.ResumeProcessorBot.repository.VacancyRepository;
import io.project.ResumeProcessorBot.service.VacancyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;


@ExtendWith(SpringExtension.class)
class VacancyServiceTest {
    @Mock
    Vacancy vacancy;
    @Mock
    VacancyRepository vacancyRepository;
    @InjectMocks
    VacancyService vacancyService;
    @Captor
    ArgumentCaptor<List<Vacancy>> vacanciesCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(vacancyService, "URL", "https://api.hh.ru/");
    }

    @Test
    void getVacancies_NotEmptyVacancyName_GetEmptyVacancyList() {
       String vacancyName = "Java Developer";
       List<Vacancy> vacancies = vacancyService.getVacancies(vacancyName);
       Mockito.when(vacancyRepository.searchByName(vacancyName)).thenReturn(Collections.emptyList());

       Mockito.verify(vacancyRepository, Mockito.atLeast(1)).searchByName(vacancyName);
       Assertions.assertEquals(Collections.emptyList(), vacancies);
    }

    @Test
    void addVacancy_NotEmptyVacancyName_SaveAllFoundVacancies() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String vacancyName = "Java Developer";

        getAddVacancyMethod().invoke(vacancyService, vacancyName);

        Mockito.verify(vacancy, Mockito.atLeastOnce()).setName(ArgumentMatchers.anyString());
        Mockito.verify(vacancy, Mockito.never()).setSchedule(ArgumentMatchers.anyString());
        Mockito.verify(vacancy, Mockito.atLeastOnce()).setRequirement(ArgumentMatchers.anyString());
        Mockito.verify(vacancy, Mockito.atLeastOnce()).setArea(ArgumentMatchers.anyString());
        Mockito.verify(vacancyRepository, Mockito.times(1)).saveAll(vacanciesCaptor.capture());
    }

    private Method getAddVacancyMethod() throws NoSuchMethodException {
        Method method = VacancyService.class.getDeclaredMethod("addVacancy", String.class);
        method.setAccessible(true);
        return method;
    }


}