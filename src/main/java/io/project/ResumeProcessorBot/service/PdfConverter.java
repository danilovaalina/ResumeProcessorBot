package io.project.ResumeProcessorBot.service;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Data
@Component
@NoArgsConstructor
@RequiredArgsConstructor

public class PdfConverter {
    private PdfReader pdfReader;

    public String getTextFromPdf(URL download) {
        StringBuilder text = new StringBuilder();
        try {
            pdfReader = new PdfReader(download);
            for (int i = 1; i <= pdfReader.getNumberOfPages(); ++i) {
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                String resultOfText = PdfTextExtractor.getTextFromPage(pdfReader, i, strategy);
                text.append(resultOfText);
            }
        } catch (IOException io) {
            System.out.println("Возникла ошибка при чтении файла");
        }
        return text.toString();
    }
}
