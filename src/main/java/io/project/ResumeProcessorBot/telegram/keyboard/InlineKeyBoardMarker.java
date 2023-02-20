package io.project.ResumeProcessorBot.telegram.keyboard;

import io.project.ResumeProcessorBot.constant.PositionButton;
import io.project.ResumeProcessorBot.constant.ProgrammingLanguageButton;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyBoardMarker {

    public  <E extends Enum<E>> InlineKeyboardMarkup getInlineMessageButtons(Class<E> buttonEnum) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (buttonEnum.equals(ProgrammingLanguageButton.class)) {
            for (ProgrammingLanguageButton languageButton : ProgrammingLanguageButton.values()) {
                rowInLine.add(getButton(languageButton.getButtonName(), languageButton.name()));
            }
        }

        if (buttonEnum.equals(PositionButton.class)) {
            for (PositionButton positionButton : PositionButton.values()) {
                rowInLine.add(getButton(positionButton.getButtonName(), positionButton.name()));
            }
        }

        rowsInLine.add(rowInLine);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private InlineKeyboardButton getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);
        return button;
    }
}
