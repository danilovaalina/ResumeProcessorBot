package io.project.ResumeProcessorBot.telegram;


import io.project.ResumeProcessorBot.config.TelegramConfig;
import io.project.ResumeProcessorBot.telegram.handler.CallbackQueryHandler;
import io.project.ResumeProcessorBot.telegram.handler.MessageHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.project.ResumeProcessorBot.telegram.component.BotCommands.LIST_OF_COMMANDS;

@Slf4j
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramBot extends TelegramLongPollingBot {
    TelegramConfig config;
    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public TelegramBot(TelegramConfig config, MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler)  {
        this.config = config;
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        try {
            execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting list of bot commands: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message != null) {
                sendMessage = messageHandler.answerMessage(message);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            sendMessage = callbackQueryHandler.processCallbackQuery(callbackQuery);
        }
        try {
            executeMessage(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
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

    private void executeMessage(SendMessage message) throws TelegramApiException {
        execute(message);
    }
}
