package io.project.ResumeProcessorBot.telegram.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum PositionButton {
    JUNIOR_BUTTON("Junior"),
    MIDDLE_BUTTON("Middle"),
    SENIOR_BUTTON("Senior");

    String buttonName;
}
