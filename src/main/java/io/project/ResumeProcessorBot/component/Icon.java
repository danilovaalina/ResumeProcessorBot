package io.project.ResumeProcessorBot.component;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Icon {
    FUNNY(":grin:"),
    SAD(":pensive:"),
    HELLO(":raised_hand:"),
    ROBOT(":robot_face:"),
    DOCUMENT(":page_facing_up:"),
    GRADE(":writing_hand:"),
    FILE(":file_folder:"),
    EXCLAMATION(":exclamation:")
    ;

    private final String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

}
