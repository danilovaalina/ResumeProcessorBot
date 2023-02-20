package io.project.ResumeProcessorBot.telegram.handler;

import io.project.ResumeProcessorBot.constant.PositionButton;
import io.project.ResumeProcessorBot.telegram.keyboard.InlineKeyBoardMarker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

import static io.project.ResumeProcessorBot.constant.PositionButton.*;
import static io.project.ResumeProcessorBot.constant.ProgrammingLanguageButton.*;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CallbackQueryHandler {

    InlineKeyBoardMarker inlineKeyBoardMarker;
    static List<String> buttonCombination = new ArrayList<>();

    public SendMessage processCallbackQuery(CallbackQuery buttonQuery) {
        long chatId = buttonQuery.getMessage().getChatId();

        String callbackData = buttonQuery.getData();

        if(callbackData.equals(JAVA_BUTTON.name()) || callbackData.equals(PYTHON_BUTTON.name())
                || callbackData.equals(GO_BUTTON.name())) {
            if(callbackData.equals(JAVA_BUTTON.name())) {
                buttonCombination.add(0, JAVA_BUTTON.name());
            } else if (callbackData.equals(PYTHON_BUTTON.name())) {
                buttonCombination.add(0, PYTHON_BUTTON.name());
            } else {
                buttonCombination.add(0, GO_BUTTON.name());
            }
            return positionCommandReceived(chatId);
        }

        if(callbackData.equals(JUNIOR_BUTTON.name()) || callbackData.equals(MIDDLE_BUTTON.name())
                || callbackData.equals(SENIOR_BUTTON.name())) {
            if (callbackData.equals(JUNIOR_BUTTON.name())) {
                buttonCombination.add(1, JUNIOR_BUTTON.name());
            } else if (callbackData.equals(MIDDLE_BUTTON.name())) {
                buttonCombination.add(1, MIDDLE_BUTTON.name());
            } else {
                buttonCombination.add(1, SENIOR_BUTTON.name());
            }
            return sendMessage(chatId, "Отправьте резюме в формате PDF");
        }

        return null;

    }

    private SendMessage positionCommandReceived(Long chatId) {
        String answer = "Выберите позицию, на которую Вы претендуете:";

        InlineKeyboardMarkup markupInLine = inlineKeyBoardMarker.getInlineMessageButtons(PositionButton.class);

        SendMessage message = sendMessage(chatId, answer);
        message.setReplyMarkup(markupInLine);

        return message;
    }

    private SendMessage sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(textToSend);
        return message;
    }

}
