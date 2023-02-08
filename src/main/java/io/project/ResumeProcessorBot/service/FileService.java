package io.project.ResumeProcessorBot.service;

import io.project.ResumeProcessorBot.config.TelegramConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@AllArgsConstructor

public class FileService {
    private final TelegramConfig config;

    private String getFileId(Document document) {
        return document.getFileId();
    }

    private URL urlForFileInfo(Document document) {
        String uploadedFileId = getFileId(document);
        URL url = null;
        try {
            url = new URL("https://api.telegram.org/bot"
                    + config.getBotToken() + "/getFile?file_id=" + uploadedFileId);
        } catch (MalformedURLException e) {
            System.out.println("Неверный URL-адрес: " + url +
                    "\nСтрока не может быть проанализирована или без надлежащего протокола");
        }
        return url;
    }

    public URL urlForDownloadFile(Document document)  {
        URL url = urlForFileInfo(document);
        URL download = null;
        try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String res = in.readLine();
            JSONObject jsonResult = new JSONObject(res);
            JSONObject path = jsonResult.getJSONObject("result");
            String file_path = path.getString("file_path");
            download = new URL("https://api.telegram.org/file/bot" + config.getBotToken() + "/" + file_path);
        }  catch (IOException io) {
            System.out.println("Не удалось получить входной поток от URL-адреса: " + url);
        } catch (JSONException js) {
            System.out.println("Произошла ошибка во время обработки JSON-объекта");
        }
        return download;
    }


}
