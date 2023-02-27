package io.project.ResumeProcessorBot.telegram.handler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static io.project.ResumeProcessorBot.telegram.constant.SpecialistButton.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CallbackQueryHandler {

    //InlineKeyBoardMarker inlineKeyBoardMarker;
    static List<String> buttonCombination;

    public SendMessage processCallbackQuery(CallbackQuery buttonQuery) {
        long chatId = buttonQuery.getMessage().getChatId();

        String callbackData = buttonQuery.getData();


        if (callbackData.equals(JAVA_JUNIOR_BUTTON.name())) {
            buttonCombination = List.of(JAVA_JUNIOR_BUTTON.name());
        } else if (callbackData.equals(JAVA_MIDDLE_BUTTON.name())) {
            buttonCombination = List.of(JAVA_MIDDLE_BUTTON.name());
        } else if (callbackData.equals(JAVA_SENIOR_BUTTON.name())) {
            buttonCombination = List.of(JAVA_SENIOR_BUTTON.name());
        } else if (callbackData.equals(PYTHON_JUNIOR_BUTTON.name())) {
            buttonCombination = List.of(PYTHON_JUNIOR_BUTTON.name());
        } else if (callbackData.equals(PYTHON_MIDDLE_BUTTON.name())) {
            buttonCombination = List.of(PYTHON_MIDDLE_BUTTON.name());
        } else if (callbackData.equals(PYTHON_SENIOR_BUTTON.name())) {
            buttonCombination = List.of(PYTHON_SENIOR_BUTTON.name());
        } else if (callbackData.equals(GO_JUNIOR_BUTTON.name())) {
            buttonCombination = List.of(GO_JUNIOR_BUTTON.name());
        } else if (callbackData.equals(GO_MIDDLE_BUTTON.name())) {
            buttonCombination = List.of(GO_MIDDLE_BUTTON.name());
        } else if (callbackData.equals(GO_SENIOR_BUTTON.name())) {
            buttonCombination = List.of(GO_SENIOR_BUTTON.name());
        } else {
           return sendMessage(chatId,"Данная специальность ещё не обрабатывается.");
        }
        return sendMessage(chatId, "Оправьте резюме в формате PDF");
        //return positionCommandReceived(chatId);

        /*
        * if (callbackData.equals(JUNIOR_BUTTON.name()) || callbackData.equals(MIDDLE_BUTTON.name())
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
        * */


    }

   /*
   * private SendMessage positionCommandReceived(Long chatId) {
        String answer = "Выберите позицию, на которую Вы претендуете:";

        InlineKeyboardMarkup markupInLine = inlineKeyBoardMarker.getInlineMessageButtons(PositionButton.class);

        SendMessage message = sendMessage(chatId, answer);
        message.setReplyMarkup(markupInLine);

        return message;
    }
   * */

    private SendMessage sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(textToSend);
        return message;
    }

}
