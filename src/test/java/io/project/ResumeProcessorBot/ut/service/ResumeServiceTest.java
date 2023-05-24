package io.project.ResumeProcessorBot.ut.service;

import io.project.ResumeProcessorBot.entity.Vacancy;
import io.project.ResumeProcessorBot.model.Resume;
import io.project.ResumeProcessorBot.service.ResumeService;
import io.project.ResumeProcessorBot.service.VacancyService;
import io.project.ResumeProcessorBot.telegram.constant.Icon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@ExtendWith(SpringExtension.class)
class ResumeServiceTest {
    @Mock
    Resume resume;
    @Mock
    VacancyService vacancyService;
    @InjectMocks
    private ResumeService resumeService;

   /* @BeforeEach
    void setUp() {
        resumeService = new ResumeService(vacancyService, resume);
    }*/

    @Test
    void getResultResume_EmptyFileText_GetRemark() {
        Mockito.when(resume.getSkills()).thenReturn(Collections.emptyList());

        String resultResume = resumeService.getResultResume("", "Python Developer");

        Assertions.assertEquals("Вы отправили пустой файл", resultResume);
        Mockito.verify(resume, Mockito.never()).getSkills();
        Mockito.verify(vacancyService, Mockito.never()).getVacancies("Python Developer");
        Assertions.assertEquals(Collections.emptyList(), resume.getSkills());
        Assertions.assertNotNull(resume.getSkills());
    }

    @Test
    void getResultResume_FileTextWithLettersAndEmptyVacancyName_GetPositiveResult() {
        Mockito.when(resume.getSkills()).thenReturn(Collections.emptyList());

        String resultResume = resumeService.getResultResume("""
                Java Developer
                Опыт работы 10 месяцев
                Учебный проект.
                Разработка интернет магазина.
                Использовались Java Core, MySql, Spring Framework,Hibernate, HTML, TypeScript, Docker, REST
                API""", "");

        Mockito.verify(resume, Mockito.times(1)).getSkills();
        Mockito.verify(vacancyService, Mockito.times(1)).getVacancies("");
        Assertions.assertEquals("Прекрасное резюме! Вы привлечёте многих работодателей!" + Icon.FUNNY.get(), resultResume);
        Assertions.assertNotNull(resume.getSkills());
    }

    @Test
    void getAbsentSkills_FileTextWithoutLettersAndNotEmptyRequirementList_GetAllSkillsFromRequirementList() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Mockito.when(resume.getSkills()).thenReturn(Collections.emptyList());

        String absentSkills = (String) getAbsentSkillsMethod().invoke(resumeService,
                ".", new ArrayList<>(Arrays.asList("Имеешь хорошее знание JVM, Collections, Stream API, Concurrency" , "Знаешь протокол HTTP, что такое REST, какие его основные принципы"))
        );

        Assertions.assertEquals("Имеешь хорошее знание JVM Collections Stream API Concurrency Знаешь протокол HTTP что такое REST какие его основные принципы ", absentSkills);
        Mockito.verify(resume, Mockito.times(1)).setSkills(Collections.emptyList());
    }

    @Test
    void getAbsentSkills_NotEmptyFileTextAndNotEmptyRequirementList_GetMissingWordsInFileText() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Mockito.when(resume.getSkills()).thenReturn(new ArrayList<>(Arrays.asList("API", "REST")));

        String absentSkills = (String) getAbsentSkillsMethod().invoke(resumeService,
                """
                        Java Developer
                        Опыт работы 10 месяцев
                        Декабрь 2021 — март 2022
                        4 месяца
                        GeekBrains
                        Intern java developer
                        Учебный проект.
                        Разработка интернет магазина.
                        Использовались Java Core, MySql, Spring Framework,Hibernate, HTML, TypeScript, Docker, REST
                        API.""",
                new ArrayList<>(Arrays.asList("Имеешь хорошее знание JVM, Collections, Stream API, Concurrency" , "Знаешь протокол HTTP, что такое REST, какие его основные принципы"))
        );

        Assertions.assertEquals("Имеешь хорошее знание JVM Collections Stream Concurrency Знаешь протокол HTTP что такое какие его основные принципы ", absentSkills);
        Mockito.verify(resume, Mockito.times(1)).setSkills(new ArrayList<>(Arrays.asList("API", "REST")));
    }

    private Method getAbsentSkillsMethod() throws NoSuchMethodException {
        Method method = ResumeService.class.getDeclaredMethod("getAbsentSkills", String.class, List.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    void getRequirements_EmptyVacancyName_GetRelevantRequirements() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Vacancy vacancy = Mockito.mock(Vacancy.class);
        getRequirementsMethod().invoke(resumeService, "Junior Fullstack Developer");

        Mockito.verify(vacancyService, Mockito.atLeast(1)).getVacancies("Junior Fullstack Developer");
        Mockito.verify(vacancy, Mockito.never()).getRequirement();
    }

    private Method getRequirementsMethod() throws NoSuchMethodException {
        Method method = ResumeService.class.getDeclaredMethod("getRequirements", String.class);
        method.setAccessible(true);
        return method;
    }
}