package io.project.ResumeProcessorBot.service;


import io.project.ResumeProcessorBot.config.TelegramConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.net.URL;

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
            URL url = null;
            try {
                Document document = update.getMessage().getDocument();
                FileService fileService = new FileService(config);
                String uploadedFileId = fileService.getFileId(document);

                url = new URL("https://api.telegram.org/bot" + config.getBotToken() + "/getFile?file_id=" + uploadedFileId);
                URL download = fileService.urlForDownloadFile(url);

                String text = new PdfConverter().getTextFromPdf(download);
                if (text.contains("Данилова") || text.contains("Алина")) {
                    sendMessage(chatId, "Прекрасное резюме! Вы привлечёте многих работодателей!");
                } else {
                    sendMessage(chatId, "Ваше ре");
                }
            }
            catch (MalformedURLException e) {
                System.out.println("Неверный URL-адрес: " + url +
                        "\nСтрока не может быть проанализирована или без надлежащего протокола");
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
        String answer = "Привет, " + name + "!" +
                "\nЯ бот, который занимается обработкой резюме и оценивает его качество. " +
                "\nВы можете отправить мне резюме в формате PDF";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Не удалось отправить сообщение пользователю");
        }
    }
}
