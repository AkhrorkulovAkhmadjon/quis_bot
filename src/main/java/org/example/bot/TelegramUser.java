package org.example.bot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.db.DB;
import org.example.unums.TelegramStatus;

import javax.ws.rs.core.NewCookie;
import java.util.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelegramUser {
    private Long chatId;
    private String phoneNumber;
    private String name;
    private int howQuestion=0;
    private TelegramStatus telegramStatus;
    private List<Integer> MsgId = new ArrayList<>();

    public boolean checkStatus(TelegramStatus telegramStatus) {
        return this.telegramStatus.equals(telegramStatus);
    }

    final  ArrayList<Integer>INTEGERS= new ArrayList<>();

    public void newTest() {
        int sized = DB.QUESTIONS.size();
        if (sized == 0) {
            return;
        }
        Random random = new Random();
        while (sized > INTEGERS.size()) {
            int index = random.nextInt(0,sized);
            if (!INTEGERS.contains(index)) {
                INTEGERS.add(index);
                System.out.println(INTEGERS);
                System.out.println(index+"daromdagi index");
            }

            System.out.println(INTEGERS);
        }

    }

}
