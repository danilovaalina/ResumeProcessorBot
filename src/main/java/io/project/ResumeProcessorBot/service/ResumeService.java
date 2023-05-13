package io.project.ResumeProcessorBot.service;

import io.project.ResumeProcessorBot.entity.Vacancy;
import io.project.ResumeProcessorBot.model.Resume;
import io.project.ResumeProcessorBot.telegram.constant.Icon;
import io.project.ResumeProcessorBot.utils.ListUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ResumeService {
   VacancyService vacancyService;
   Resume resume;

   public String getResultResume(@NotNull String fileText, String vacancyName) {
      if (fileText.isEmpty() | fileText.isBlank()) return "Вы отправили пустой файл";

      List<String> requirementList = getRequirements(vacancyName);

      List<String> tempArray = ListUtils.getPhrasesSentenceList(requirementList);

      List<String> tempArrayWithoutDuplicates = tempArray.stream()
              .distinct().toList();

      String absentSkills = getAbsentSkills(fileText, requirementList);

      if (resume.getSkills().size() < tempArrayWithoutDuplicates.size() / 2) {
         return "Ваше резюме не до конца проработано" + Icon.SAD.get()
                 + "\nВам нужно изучить следующие технологии и применить их в своём pet-проекте как подтверждение своих знаний:\n"
                 + absentSkills + "\nЖду изменений! Удачи! " + Icon.LUCK.get();
      } else {
         return "Прекрасное резюме! Вы привлечёте многих работодателей!" + Icon.FUNNY.get();
      }
   }

   private @NotNull String getAbsentSkills(String fileText, List<String> requirementList) {
      List<String> sentenceList = ListUtils.getSentenceList(fileText);
      List<List<String>> sentenceLists = ListUtils.turnListIntoListOfLists(sentenceList);

      List<List<String>> requirementLists = ListUtils.turnListIntoListOfLists(requirementList);

      List<String> matchList = ListUtils.getMatchList(requirementLists, sentenceLists);

      resume.setSkills(matchList);

      int i = 0;
      for (String requirement : requirementList) {
         for (String match : matchList) {
            if (requirement.contains(match)){
               requirementList.set(i, requirement.replaceAll(match, ""));
               i++;
            }
         }
      }

      List<String> tempList = ListUtils.getPhrasesSentenceList(requirementList);

      List<String> tempListWithoutDuplicates = tempList.stream()
              .distinct().toList();

      StringBuilder absentSkills = new StringBuilder();
      for (String temp : tempListWithoutDuplicates) {
         absentSkills.append(temp.trim());
         absentSkills.append(" ");
      }

      return absentSkills.toString();
   }

   private @NotNull List<String> getRequirements(String vacancyName) {
      StringBuilder requirements = new StringBuilder();
      for (Vacancy vacancy : vacancyService.getVacancies(vacancyName)) {
         requirements.append(vacancy.getRequirement());
      }

      return ListUtils.getSentenceList(requirements.toString());
   }
}
