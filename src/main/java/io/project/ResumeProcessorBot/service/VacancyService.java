package io.project.ResumeProcessorBot.service;

import io.project.ResumeProcessorBot.entity.Vacancy;
import io.project.ResumeProcessorBot.repository.VacancyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VacancyService {
    final VacancyRepository vacancyRepository;
    final Vacancy vacancy;
    @Value("${hh-ru.api-url}")
    String URL;

    public List<Vacancy> getVacancies(String vacancyName) {
        List<Vacancy> vacancies = vacancyRepository.searchByName(vacancyName);
        if (vacancies.isEmpty()) {
            addVacancy(vacancyName);
            vacancies = vacancyRepository.searchByName(vacancyName);
        }
        return vacancies;
    }

    private void addVacancy(String vacancyName) {
        List<Vacancy> vacancies = new ArrayList<>();
        String result = getResult(vacancyName);
        JSONObject jsonObject = new JSONObject(result);
        JSONArray items = jsonObject.getJSONArray("items");
        for(int i = 0; i < items.length(); i++) {
            JSONObject jObj = items.getJSONObject(i);
            if (jObj.has("name")) {
                vacancy.setName(jObj.getString("name"));
            }
            if (jObj.has("schedule") && !jObj.isNull("schedule")) {
                vacancy.setSchedule(jObj.getString("schedule"));
            }
            JSONObject snippet = jObj.getJSONObject("snippet");
            if (snippet.has("requirement")) {
                vacancy.setRequirement(snippet.getString("requirement").
                        replaceAll("<highlighttext>", "").replaceAll("</highlighttext>", ""));
            }
            JSONObject area = jObj.getJSONObject("area");
            if (area.has("name")) {
                vacancy.setArea(area.getString("name"));
            }
            vacancies.add(vacancy);
        }
        vacancyRepository.saveAll(vacancies);
    }

    private String getResult(String vacancyName) {
        StringBuilder result = new StringBuilder();
        try {
            String paramValue = URLEncoder.encode(vacancyName, StandardCharsets.UTF_8);
            URL url = new URL(MessageFormat.format("{0}vacancies?text={1}", URL, paramValue));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String USER_AGENT = "Mozilla/5.0";
            connection.setRequestProperty("User-Agent", USER_AGENT);
            Scanner scanner = new Scanner((InputStream) connection.getContent());
            while(scanner.hasNextLine()) {
                result.append(scanner.nextLine());
            }
        } catch (IOException e) {
            log.error("Неверный URL-адрес"  +
                    "\nСтрока не может быть проанализирована или без надлежащего протокола");
        }
        return result.toString();
    }
}
