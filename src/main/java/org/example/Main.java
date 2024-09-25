package org.example;

import lombok.SneakyThrows;
import org.example.bot.MyBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi= new TelegramBotsApi(DefaultBotSession.class);
       telegramBotsApi.registerBot(new MyBot());
    }
}