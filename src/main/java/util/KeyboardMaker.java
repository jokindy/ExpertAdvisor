package util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardMaker {

    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Получить совет"));
        row1.add(new KeyboardButton("Скачать меню"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Связаться"));
        row2.add(new KeyboardButton("Помощь"));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }

    public static List<List<InlineKeyboardButton>> getButtons(List<String> descriptions) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String description : descriptions) {
            buttons.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(description)
                                    .callbackData(description)
                                    .build()));
        }
        return buttons;
    }
}
