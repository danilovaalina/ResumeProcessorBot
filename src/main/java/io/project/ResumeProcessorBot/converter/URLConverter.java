package io.project.ResumeProcessorBot.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
@Component
public class URLConverter {

    public static byte[] getByteArrayUrl(URL url) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = url.openStream()) {
            byte [] bytesChunk = new byte[4096];
            int bytesRead;

            while((bytesRead = inputStream.read(bytesChunk)) > 0) {
                outputStream.write(bytesChunk, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (IOException io) {
            log.error("Не удалось получить входной поток от URL-адреса: " + url);
        }
        return null;
    }
}
