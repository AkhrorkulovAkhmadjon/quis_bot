package org.example.db;

import org.example.adminService.Question;
import org.example.bot.TelegramUser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface DB {
    ConcurrentHashMap<Long, TelegramUser>TG_USER=new ConcurrentHashMap<>();
    ConcurrentHashMap<UUID,UUID>ANSWER_CHECK=new ConcurrentHashMap<>();
    ArrayList<Question>QUESTIONS= new ArrayList<>();

}
