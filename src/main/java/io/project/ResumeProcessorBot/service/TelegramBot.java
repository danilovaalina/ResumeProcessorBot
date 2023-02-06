package io.project.ResumeProcessorBot.service;



import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import io.project.ResumeProcessorBot.config.TelegramConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.*;

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

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                default:
                    sendMessage(chatId, "Извините, данная комманда не поддерживается");
            }
        }

        if(update.hasMessage() && update.getMessage().hasDocument()) {
            String uploadedFileId = update.getMessage().getDocument().getFileId();

            try {
                URL url = new URL("https://api.telegram.org/bot"+config.getBotToken()+"/getFile?file_id="+uploadedFileId);
                BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
                String res = in.readLine();
                JSONObject jresult = new JSONObject(res);
                JSONObject path = jresult.getJSONObject("result");
                String file_path = path.getString("file_path");
                URL download = new URL("https://api.telegram.org/file/bot" + config.getBotToken() + "/" + file_path);
                PdfReader pdfReader = new PdfReader(download);
                for (int i = 1; i <= pdfReader.getNumberOfPages(); ++i) {
                    TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                    String text = PdfTextExtractor.getTextFromPage(pdfReader, i, strategy);
                    if (text.contains("Данилова") || text.contains("Алина")) {
                        sendMessage(chatId, "Прекрасное резюме! Вы привлечёте многих работодателей!");
                    } else {
                        sendMessage(chatId, "Ну резюме как-то так себе. Вам есть над чем поработать");
                        break;
                    }
                }
            } catch (IOException | JSONException e) {
                System.out.println("Произошла ошибка в результате обработки файла");
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

        }
    }
}
