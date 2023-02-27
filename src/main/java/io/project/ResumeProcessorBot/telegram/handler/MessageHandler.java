package io.project.ResumeProcessorBot.telegram.handler;

import io.project.ResumeProcessorBot.telegram.constant.Icon;
import io.project.ResumeProcessorBot.telegram.constant.SpecialistButton;
import io.project.ResumeProcessorBot.service.FileService;
import io.project.ResumeProcessorBot.telegram.keyboard.InlineKeyBoardMarker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static io.project.ResumeProcessorBot.telegram.constant.SpecialistButton.*;

@Slf4j
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {
    InlineKeyBoardMarker inlineKeyBoardMarker;
    FileService fileService;

    public SendMessage answerMessage(Message message) {
        long chatId = message.getChatId();

        if (message.hasDocument()) {
            Document document = message.getDocument();
            return sendMessage(chatId, processFile(document));
        }

        String inputText = message.getText();
        String name = message.getChat().getFirstName();

        switch (inputText) {
            case "/start" -> {
                return startMessage(chatId, name);
            }
            case "/help" -> {
                return helpMessage(chatId, name);
            }
            case "/next" -> {
                return nextMessage(chatId);
            }
            default -> {
                return sendMessage(chatId, "Извините, данная комманда не поддерживается");
            }
        }
    }

    private SendMessage startMessage(Long chatId, String name) {
        String answer = "Привет, " + name + "!" + Icon.HELLO.get()
                + "\nДавай оценим твоём резюме!" + Icon.GRADE.get()
                + "\nЕсли Вам нужна помощь, нажмите /help"
                + "\nЕсли Вы хотите продолжить, нажмите /next";

        log.info("Replied to user " + name);
        return sendMessage(chatId, answer);
    }

    private SendMessage helpMessage(Long chatId, String name) {
        String answer = "Я бот" + Icon.ROBOT.get()
                + ", который занимается обработкой резюме в сфере программирования" + Icon.COMPUTER.get()
                + " и оценивает его качество" + Icon.GRADE.get() +
                "\nВы можете отправить мне резюме в формате PDF" + Icon.FILE.get() +
                "\n\n" + Icon.EXCLAMATION.get() + "Список команд:" +
                "\n\n/start" + "\n\n/help" + "\n\n/settings" + "\n\n/next";

        log.info("Replied to user " + name);
        return sendMessage(chatId, answer);
    }

    private SendMessage nextMessage(Long chatId) {
        String answer = "Выберите язык программирования, с которым вы работаете:";

        InlineKeyboardMarkup markupInLine = inlineKeyBoardMarker.getInlineMessageButtons(SpecialistButton.class);

        SendMessage sendMessage = sendMessage(chatId, answer);
        sendMessage.setReplyMarkup(markupInLine);
        return sendMessage;
    }

    private SendMessage sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(textToSend);
        return message;
    }

    private String processFile(Document document) {
        String textToSend = null;
        List<String> buttonCombination = CallbackQueryHandler.buttonCombination;
        if (buttonCombination.size() == 1) {
            if (buttonCombination.get(0).equals(JAVA_JUNIOR_BUTTON.name())) {
                  textToSend = fileService.processFile(document, 3L);
            } else if (buttonCombination.get(0).equals(JAVA_MIDDLE_BUTTON.name())) {
                textToSend = fileService.processFile(document, 4L);
            } else if (buttonCombination.get(0).equals(JAVA_SENIOR_BUTTON.name())) {
                textToSend = fileService.processFile(document, 5L);
            } else if (buttonCombination.get(0).equals(PYTHON_JUNIOR_BUTTON.name())) {
                textToSend = fileService.processFile(document, 6L);
            } else if (buttonCombination.get(0).equals(PYTHON_MIDDLE_BUTTON.name())) {
                textToSend = fileService.processFile(document, 7L);
            } else if (buttonCombination.get(0).equals(PYTHON_SENIOR_BUTTON.name())) {
                textToSend = fileService.processFile(document, 8L);
            } else if (buttonCombination.get(0).equals(GO_JUNIOR_BUTTON.name())) {
                textToSend = fileService.processFile(document, 9L);
            } else if (buttonCombination.get(0).equals(GO_MIDDLE_BUTTON.name())) {
                textToSend = fileService.processFile(document, 10L);
            } else if (buttonCombination.get(0).equals(GO_SENIOR_BUTTON.name())) {
                textToSend = fileService.processFile(document, 11L);
            }
        } else {
            textToSend = "Не удалось обработать файл " + Icon.SAD.get() + " Нажмите команду /next, чтобы попробовать снова";
        }
        return textToSend;
    }

}
