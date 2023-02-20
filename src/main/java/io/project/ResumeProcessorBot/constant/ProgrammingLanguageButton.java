package io.project.ResumeProcessorBot.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ProgrammingLanguageButton {
    JAVA_BUTTON("Java"),
    PYTHON_BUTTON("Python"),
    GO_BUTTON("Go");

    String buttonName;
}
