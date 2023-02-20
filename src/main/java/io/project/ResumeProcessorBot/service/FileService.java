package io.project.ResumeProcessorBot.service;

import io.project.ResumeProcessorBot.component.Icon;
import io.project.ResumeProcessorBot.config.TelegramConfig;
import io.project.ResumeProcessorBot.database.JavaDeveloper;
import io.project.ResumeProcessorBot.database.JavaDeveloperRepository;
import io.project.ResumeProcessorBot.database.TechnologyStack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileService {
    TelegramConfig config;
    JavaDeveloperRepository repository;

    private  String getFileId(Document document) {
        return document.getFileId();
    }

    private  URL urlForFileInfo(Document document) {
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

    private String textToSend(int count, String[] technologies, StringBuilder unexploredTechnologies) {
        String textToSend;
        if (count <= technologies.length/2) {
            textToSend = "Ваше резюме не до конца проработано" + Icon.SAD.get()
                    + "\nВам нужно изучить следующие технологии и применить их в своём pet-проекте как подтверждение своих знаний:\n"
                    + unexploredTechnologies + "Жду изменений! Удачи! " + Icon.LUCK.get();
        } else {
            textToSend = "Прекрасное резюме! Вы привлечёте многих работодателей!" + Icon.FUNNY.get();
        }
        return textToSend;
    }

    public  String processFileForJavaDeveloper(Document document, Long positionId) {
        URL download = urlForDownloadFile(document);
        String text = new PdfConverter().getTextFromPdf(download);
        Optional<JavaDeveloper> developer= repository.findById(positionId);
        List<TechnologyStack> technologyStacks = new ArrayList<>();
        if (developer.isPresent()) {
            technologyStacks = developer.get().getTechnologyStack();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (TechnologyStack stack : technologyStacks) {
            stringBuilder.append(stack.getTechnology());
            stringBuilder.append(" ");
        }
        String [] technologies = stringBuilder.toString().split(" ");
        StringBuilder unexploredTechnologies = new StringBuilder();
        int count = 0;
        for (String technology : technologies) {
            if (text.toUpperCase().contains(technology.toUpperCase())) {
                count++;
            } else {
                unexploredTechnologies.append(technology);
                unexploredTechnologies.append("\n");
            }
        }

        return textToSend(count, technologies, unexploredTechnologies);
    }


}
