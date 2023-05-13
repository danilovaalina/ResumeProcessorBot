package io.project.ResumeProcessorBot.service;

import io.project.ResumeProcessorBot.converter.PdfConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileService {
    PdfConverter pdfConverter;

    public String getFileText(byte[] bytesFile) {
        return pdfConverter.getTextFromPdf(bytesFile);
    }
}
