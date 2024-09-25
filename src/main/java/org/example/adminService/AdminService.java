package org.example.adminService;

import lombok.SneakyThrows;
import org.example.bot.TelegramUser;
import org.example.db.DB;
import org.example.unums.TelegramStatus;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminService {
    public static Question carin;

    @SneakyThrows
    public static SendMessage userAdminBot(TelegramUser user, Update update) {
        if (update.getMessage() != null){
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(user.getChatId());
            if (user.checkStatus(TelegramStatus.START)) {
                sendMessage.setText("Assalomu aleykum Admin");
                sendMessage.setChatId(user.getChatId());
                List<KeyboardRow> rows = new ArrayList<>();
                KeyboardRow row = new KeyboardRow();
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setText("Yangi savol");
                row.add(keyboardButton);
                rows.add(row);
                ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
                markup.setKeyboard(rows);
                markup.setSelective(true);
                markup.setResizeKeyboard(true);
                markup.setOneTimeKeyboard(true);
                user.setTelegramStatus(TelegramStatus.NEW_QUESTION);
                sendMessage.setReplyMarkup(markup);
                return sendMessage;
            } else if (message.getText().equals("Yangi savol")) {
                user.setTelegramStatus(TelegramStatus.NEW_QUESTION);
                sendMessage.setText("Savolni yuboring");
                return sendMessage;
            }else if (user.checkStatus(TelegramStatus.NEW_QUESTION)){
                Question question =Question.builder()
                        .question(message.getText())
                        .build();
                carin=question;
                sendMessage.setText("Savol varyatlarini yozing savol ->"+question.getQuestion());
                user.setTelegramStatus(TelegramStatus.VAR_YANT);
                return sendMessage;
            }else if (user.checkStatus(TelegramStatus.VAR_YANT)){
                carin.getAnswers().add(new Answers(message.getText()));
                if (carin.getAnswers().size()==1){
                    sendMessage.setText("Ikkinchi varyantni kiriting");
                } else if (carin.getAnswers().size()==2) {
                    sendMessage.setText("Uchinchi varyantni kiriting");
                } else if (carin.getAnswers().size()==3) {
                    DB.QUESTIONS.add(carin);
                    sendMessage.setReplyMarkup(correctAnswer(carin));
                    sendMessage.setText(carin.getQuestion());
                    carin=null;
                    user.setTelegramStatus(TelegramStatus.CHOOSE_ANSR);
                }

                return sendMessage;
            }
        }


        SendMessage message = new SendMessage();
        return message;
    }

    private static ReplyKeyboard correctAnswer(Question carin) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonInl = new InlineKeyboardButton();
        buttonInl.setText(carin.getAnswers().get(0).getAnswer());
        buttonInl.setCallbackData(carin.getAnswers().get(0).getId().toString());

        InlineKeyboardButton buttonInl1 = new InlineKeyboardButton();
        buttonInl1.setText(carin.getAnswers().get(1).getAnswer());
        buttonInl1.setCallbackData(carin.getAnswers().get(1).getId().toString());

        InlineKeyboardButton buttonInl2 = new InlineKeyboardButton();
        buttonInl2.setText(carin.getAnswers().get(2).getAnswer());
        buttonInl2.setCallbackData(carin.getAnswers().get(2).getId().toString());

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(new ArrayList<>(Collections.singletonList(buttonInl)));
        rows.add(new ArrayList<>(Collections.singletonList(buttonInl1)));
        rows.add(new ArrayList<>(Collections.singletonList(buttonInl2)));

        markup.setKeyboard(rows);
        return markup;
    }
}
