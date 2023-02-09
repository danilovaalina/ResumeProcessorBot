package io.project.ResumeProcessorBot.service;


import io.project.ResumeProcessorBot.config.TelegramConfig;
import io.project.ResumeProcessorBot.component.Icon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramConfig config;

    public TelegramBot(TelegramConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get introductory message"));
        listOfCommands.add(new BotCommand("/help", "info how to use bot"));
        listOfCommands.add(new BotCommand("/settings", "set your preferences"));
        listOfCommands.add(new BotCommand("/next", "continue"));
        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting list of bot commands: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getChat().getFirstName();
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, name);
                case "/help" -> helpCommandReceived(chatId, name);
                default -> sendMessage(chatId, "Извините, данная комманда не поддерживается");
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
                + "\nДавай оценим твоём резюме!" + Icon.GRADE.get()
                + "\nЕсли Вам нужна помощь, нажмите /help"
                + "\nЕсли Вы хотите продолжить, нажмите /next";

        log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void helpCommandReceived(Long chatId, String name) {
        String answer = "Я бот" + Icon.ROBOT.get()
                + ", который занимается обработкой резюме" + Icon.DOCUMENT.get()
                + " и оценивает его качество" + Icon.GRADE.get() +
                "\nВы можете отправить мне резюме в формате PDF" + Icon.FILE.get() +
                "\n\n" + Icon.EXCLAMATION.get() + "Список команд:" +
                "\n\n/start" + "\n\n/help" + "\n\n/settings" + "\n\n/next";

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
