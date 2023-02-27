package io.project.ResumeProcessorBot.telegram.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum SpecialistButton {
    JAVA_JUNIOR_BUTTON("Junior Java Developer"),

    JAVA_MIDDLE_BUTTON("Middle Java Developer"),

    JAVA_SENIOR_BUTTON("Senior Java Developer"),

    PYTHON_JUNIOR_BUTTON("Junior Python Developer"),

    PYTHON_MIDDLE_BUTTON("Middle Python Developer"),

    PYTHON_SENIOR_BUTTON("Senior Python Developer"),

    GO_JUNIOR_BUTTON("Junior Go Developer"),

    GO_MIDDLE_BUTTON("Middle Go Developer"),

    GO_SENIOR_BUTTON("Senior Go Developer");

    String buttonName;
}
