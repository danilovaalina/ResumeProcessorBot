package io.project.ResumeProcessorBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class Resume {

    int experience;

    List<String> skills;
}
