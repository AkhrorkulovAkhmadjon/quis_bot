package org.example.bot;

import lombok.SneakyThrows;
import org.example.adminService.AdminService;
import org.example.adminService.Answers;
import org.example.adminService.Question;
import org.example.db.DB;
import org.example.unums.TelegramStatus;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class MyBot extends TelegramLongPollingBot {

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null) {
            Message message = update.getMessage();
            Long id = message.getChatId();
            TelegramUser user = checkingUser(id);
            if (user.getChatId().equals(6092297532l)) {
                if (message.getText().equals("/start")) {
                    user.setTelegramStatus(TelegramStatus.START);
                }
                SendMessage sendMessage = AdminService.userAdminBot(user, update);
                execute(sendMessage);
                return;
            }
            if (message.getText() != null) {
                if (message.getText().equals("/start")) {
                    SendMessage sendMessage = BotService.askContact(user, message);
                    user.setTelegramStatus(TelegramStatus.SHARE_CONTACT);
                    user.newTest();
                    execute(sendMessage);
                }
            } else if (message.hasContact()) {
                if (user.checkStatus(TelegramStatus.SHARE_CONTACT)) {
                    user.setName(message.getContact().getPhoneNumber());
                    user.checkStatus(TelegramStatus.QUESTION_AND_ANSWER);
                    execute(choseQuestion(user));
                }
            }
        } else if (update.getCallbackQuery() != null) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long id = callbackQuery.getMessage().getChatId();
            TelegramUser user = checkingUser(id);
            if (id.equals(6092297532l)) {
                if (user.checkStatus(TelegramStatus.CHOOSE_ANSR)) {
                    for (Question question : DB.QUESTIONS) {
                        for (Answers answer : question.getAnswers()) {
                            if (answer.getId().toString().equals(callbackQuery.getData())) {
                                DB.ANSWER_CHECK.put(question.getId(), answer.getId());
                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setText("Savol mofaqiyatli yuklandi");
                                sendMessage.setChatId(id);
                                user.setTelegramStatus(TelegramStatus.START);
                                execute(sendMessage);
                                return;
                            }
                        }
                    }
                }
            }
            if (callbackQuery.getData() != null) {
                String data = callbackQuery.getData();
                for (Question question : DB.QUESTIONS) {
                    for (Answers answers : question.getAnswers()) {
                        if (answers.getId().toString().equals(data)) {
                            UUID uuid = DB.ANSWER_CHECK.get(question.getId());
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(id);
                            if (uuid.toString().equals(data)) {
                                sendMessage.setText("✅ To'gri javob ");
                            } else {
                                sendMessage.setText("❌ Noto'gri javob ");
                            }
                            SendMessage sendMessage1 = choseQuestion(user);
                            if (DB.QUESTIONS.size() >= user.getHowQuestion() - 1) {
                                String string = sendMessage1.getText();
                                sendMessage1.setText(sendMessage.getText() + "\n" + string);
                            }
                            execute(sendMessage1);
                        }
                    }
                }
            }
        }
    }

    private SendMessage choseQuestion(TelegramUser user) {
        Long id = user.getChatId();
        int howQuestion = 0;
        try {
            howQuestion = user.getINTEGERS().get(user.getHowQuestion());
        } catch (IndexOutOfBoundsException e) {
            if (user.getHowQuestion() >= DB.QUESTIONS.size()) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Savollar hozirchalik tugadi");
                sendMessage.setChatId(id);
                return sendMessage;
            }
        }

        SendMessage sendMessage = startQuestion(DB.QUESTIONS.get(howQuestion));
        sendMessage.setText(DB.QUESTIONS.get(howQuestion).getQuestion());
        sendMessage.setChatId(id);
        user.setHowQuestion(user.getHowQuestion() + 1);
        return sendMessage;
    }


    private SendMessage startQuestion(Question question) {

        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(question.getAnswers().get(0).getAnswer());
        button.setCallbackData(question.getAnswers().get(0).getId().toString());

        InlineKeyboardButton buttonInl1 = new InlineKeyboardButton();
        buttonInl1.setText(question.getAnswers().get(1).getAnswer());
        buttonInl1.setCallbackData(question.getAnswers().get(1).getId().toString());

        InlineKeyboardButton buttonInl2 = new InlineKeyboardButton();
        buttonInl2.setText(question.getAnswers().get(2).getAnswer());
        buttonInl2.setCallbackData(question.getAnswers().get(2).getId().toString());

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(new ArrayList<>(Collections.singletonList(button)));
        rows.add(new ArrayList<>(Collections.singletonList(buttonInl1)));
        rows.add(new ArrayList<>(Collections.singletonList(buttonInl2)));
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    private TelegramUser checkingUser(Long id) {
        TelegramUser orDefault = DB.TG_USER.getOrDefault(id, null);
        if (orDefault != null) {
            return orDefault;
        }
        TelegramUser user = TelegramUser.builder()
                .chatId(id).telegramStatus(TelegramStatus.START)
                .build();
        DB.TG_USER.put(id, user);
        return user;
    }

    @Override
    public String getBotUsername() {
        return "https://t.me/G43First_bot";
    }

    @Override
    public String getBotToken() {
        return "7259596500:AAELO0ggZ4deiaCmOi3NM5N6yWhOHezHZWA";
    }
}
