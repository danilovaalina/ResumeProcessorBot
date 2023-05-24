package io.project.ResumeProcessorBot.telegram.service;

import io.project.ResumeProcessorBot.converter.URLConverter;
import io.project.ResumeProcessorBot.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentService {
    final FileService fileService;
    @Value("${telegram.api-url}")
    String URL;

    @Value("${telegram.bot-token}")
    String botToken;

    public String getFileText(Document document) {
        URL url = getUrlDownloadFile(document);
        byte[] bytesFile = URLConverter.getByteArrayUrl(url);
        return fileService.getFileText(bytesFile);
    }
    private URL getUrlDownloadFile(Document document) {
        URL urlFileInfo = getUrlFileInfo(document);
        URL urlDownloadFile = null;
        try(BufferedReader in = new BufferedReader(new InputStreamReader(urlFileInfo.openStream()))) {
            String result = in.readLine();
            JSONObject jsonResult = new JSONObject(result);
            JSONObject path = jsonResult.getJSONObject("result");
            String filePath = path.getString("file_path");
            urlDownloadFile = new URL(MessageFormat.format("{0}/file/bot{1}/{2}", URL, botToken, filePath));
        }  catch (IOException io) {
            log.error("Не удалось получить входной поток от URL-адреса: " + urlFileInfo);
        } catch (JSONException js) {
            log.error("Произошла ошибка во время обработки JSON-объекта");
        }
        return urlDownloadFile;
    }

    private URL getUrlFileInfo(Document document) {
        String fileId = document.getFileId();
        URL urlFileInfo = null;
        try {
            urlFileInfo = new URL(MessageFormat.format("{0}bot{1}/getFile?file_id={2}", URL, botToken, fileId));
        } catch (MalformedURLException e) {
            log.error("Неверный URL-адрес" +
                    "\nСтрока не может быть проанализирована или без надлежащего протокола");
        }
        return urlFileInfo;
    }

}
