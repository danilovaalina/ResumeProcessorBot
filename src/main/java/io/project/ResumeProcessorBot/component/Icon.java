package io.project.ResumeProcessorBot.component;

import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Icon {
    FUNNY(":grin:"),
    SAD(":pensive:"),
    LUCK(":pray:"),
    HELLO(":raised_hand:"),
    ROBOT(":robot_face:"),
    DOCUMENT(":page_facing_up:"),
    GRADE(":writing_hand:"),
    FILE(":file_folder:"),
    COMPUTER(":computer:"),
    EXCLAMATION(":exclamation:")
    ;

    String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

}
