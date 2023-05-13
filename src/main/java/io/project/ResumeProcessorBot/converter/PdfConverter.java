package io.project.ResumeProcessorBot.converter;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@NoArgsConstructor
public class PdfConverter {

    public String getTextFromPdf(byte[] bytesFile) {
        StringBuilder text = new StringBuilder();
        try {
            PdfReader pdfReader = new PdfReader(bytesFile);
            for (int i = 1; i <= pdfReader.getNumberOfPages(); ++i) {
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                String resultOfText = PdfTextExtractor.getTextFromPage(pdfReader, i, strategy);
                text.append(resultOfText);
            }
        } catch (IOException e) {
            log.error("Возникла ошибка при чтении файла");
        }
        return text.toString();
    }
}
