package io.project.ResumeProcessorBot.ut.service;

import io.project.ResumeProcessorBot.converter.PdfConverter;
import io.project.ResumeProcessorBot.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
@ExtendWith(SpringExtension.class)
class FileServiceTest {
    @Mock
    PdfConverter pdfConverter;

    @InjectMocks
    FileService fileService;

    @Test
    void getFileText() {
        String inputString = """
                Java Developer
                Опыт работы 10 месяцев
                Учебный проект.
                Разработка интернет магазина.
                Использовались Java Core, MySql, Spring Framework,Hibernate, HTML, TypeScript, Docker, REST
                API""";
        byte[] byteArray = inputString.getBytes();

        String text = fileService.getFileText(byteArray);

        Assertions.assertNull(text);
        Mockito.verify(pdfConverter, Mockito.times(1)).getTextFromPdf(byteArray);
    }
}