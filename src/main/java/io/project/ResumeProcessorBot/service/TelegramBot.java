package io.project.ResumeProcessorBot.service;


import io.project.ResumeProcessorBot.config.TelegramConfig;
import io.project.ResumeProcessorBot.component.Icon;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URL;
@Slf4j
@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramConfig config;

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if ("/start".equals(messageText)) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else {
                sendMessage(chatId, "Извините, данная комманда не поддерживается");
            }
        }

        if (update.hasMessage() && update.getMessage().hasDocument()) {
            Document document = update.getMessage().getDocument();
            FileService fileService = new FileService(config);
            URL download = fileService.urlForDownloadFile(document);
            String text = new PdfConverter().getTextFromPdf(download);
            if (text.contains("Данилова") || text.contains("Алина")) {
                sendMessage(chatId, "Прекрасное резюме! Вы привлечёте многих работодателей!" + Icon.FUNNY.get());
            } else {
                sendMessage(chatId, "Ваше резюме не до конца проработано" + Icon.SAD.get() + " Жду изменений!");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Привет, " + name + "!" + Icon.HELLO.get()
                + "\nЯ бот" + Icon.ROBOT.get()
                + ", который занимается обработкой резюме" + Icon.DOCUMENT.get()
                + " и оценивает его качество" + Icon.GRADE.get() +
                "\nВы можете отправить мне резюме в формате PDF" + Icon.FILE.get();

        log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение пользователю");
        }
    }
}
