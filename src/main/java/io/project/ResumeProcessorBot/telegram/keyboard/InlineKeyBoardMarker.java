package io.project.ResumeProcessorBot.telegram.keyboard;

import io.project.ResumeProcessorBot.telegram.constant.SpecialistButton;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyBoardMarker {

    public  <E extends Enum<E>> InlineKeyboardMarkup getInlineMessageButtons(Class<E> buttonEnum) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        if (buttonEnum.equals(SpecialistButton.class)) {
            for (SpecialistButton languageButton : SpecialistButton.values()) {
                rowsInLine.add(getButton(languageButton.getButtonName(), languageButton.name()));
            }
        }

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        rowInLine.add(button);
        return rowInLine;
    }
}
