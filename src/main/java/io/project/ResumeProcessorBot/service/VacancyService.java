package io.project.ResumeProcessorBot.service;

import io.project.ResumeProcessorBot.model.vacancy.Vacancy;
import lombok.AllArgsConstructor;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@AllArgsConstructor
public class VacancyService {
    private final String USER_AGENT = "Mozilla/5.0";

    @Value("${app.default-page-size}")
    private int defaultPageSize;

    @Value("${url.hh-ru.vacancies}")
    private String urlVacancies;

    private RestTemplate template;




    private String getUrl(Vacancy vacancy, Integer page) {
        page = page == null ? 0 : page;
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(urlVacancies)).newBuilder();

        urlBuilder.addQueryParameter("page", String.valueOf(page));
        urlBuilder.addQueryParameter("per_page", String.valueOf(defaultPageSize));

        if (vacancy != null) {
            urlBuilder.addQueryParameter("experience", vacancy.getExperience());
            urlBuilder.addQueryParameter("employment", vacancy.getEmployment());
            urlBuilder.addQueryParameter("schedule", vacancy.getSchedule());
            urlBuilder.addQueryParameter("text", vacancy.getText());
            urlBuilder.addQueryParameter("only_with_salary", String.valueOf(vacancy.getOnlyWithSalary()));
        }

        return urlBuilder.toString();
    }



}
