package io.project.ResumeProcessorBot.telegram.component;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "get introductory message"),
            new BotCommand("/help", "info how to use bot"),
            new BotCommand("/settings", "set your preferences"),
            new BotCommand("/next", "continue")
    );
}
