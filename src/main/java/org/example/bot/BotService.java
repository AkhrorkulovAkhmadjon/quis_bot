package org.example.bot;

import org.example.adminService.Question;
import org.example.db.DB;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BotService {

    public static SendMessage askContact(TelegramUser user, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Botdan to'liq foydalanish uchun kontaktingizni yuboring");
        sendMessage.setChatId(user.getChatId());
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Kantaktni yuborish");
        keyboardButton.setRequestContact(true);
        row.add(keyboardButton);
        rows.add(row);
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(rows);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }


}
