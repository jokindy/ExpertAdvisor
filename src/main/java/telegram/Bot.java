package telegram;

import entity.Contact;
import entity.Taste;
import lombok.SneakyThrows;
import mix.Greetings;
import mix.Mix;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import util.KeyboardMaker;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "5420641143:AAGx8dR0BrTcbk2E_e_zMV_UZGVFRMiePbM";
    private static final String USERNAME = "@ExpertBarAdvisorBot";
    private static final File menu = new File("resources\\menu2021.pdf");

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleCallback(CallbackQuery callbackQuery) {
        String taste = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        Random random = new Random();
        switch (taste) {
            case "Сладенькое" -> {
                int size = Mix.sweetMixes.size();
                String advice = Mix.sweetMixes.get(random.nextInt(size));
                fillWaiting(chatId, Greetings.sweetGreetings);
                execute(getMessage(chatId, advice));
            }
            case "Кисленькое" -> {
                int size = Mix.sourMixes.size();
                String advice = Mix.sourMixes.get(random.nextInt(size));
                fillWaiting(chatId, Greetings.sourGreetings);
                execute(getMessage(chatId, advice));
            }
            case "Странное" -> {
                int size = Mix.strangeMixes.size();
                String advice = Mix.strangeMixes.get(random.nextInt(size));
                fillWaiting(chatId, Greetings.strangeGreetings);
                execute(getMessage(chatId, advice));
            }
            case "Двойное яблоко" -> {
                execute(getMessage(chatId, Greetings.doubleAppleGreetings.get(random.nextInt(3))));
                TimeUnit.SECONDS.sleep(1);
                execute(getMessage(chatId, "Больной ублюдок..."));
            }
            case "Вконтакте" -> execute(getMessage(chatId, "vk.com/expertbar"));
            case "Телеграм" -> execute(getMessage(chatId, "@ExpertBar"));
            case "Инстаграм" -> execute(getMessage(chatId, "instagram.com/expertbar"));
            case "Телефон" -> execute(getMessage(chatId, "+79219135164"));
        }
        TimeUnit.SECONDS.sleep(1);
        execute(getMessage(chatId, "Спасибо за использование! Выберете команду:"));
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.hasText()) {
            String command = message.getText();
            switch (command) {
                case "/start" -> execute(SendMessage.builder()
                        .text("Привет! Я помогу тебе выбрать вкусный кальян в Эксперте! Используй клавиатуру, " +
                                "чтобы начать работу")
                        .replyMarkup(KeyboardMaker.getMainMenuKeyboard())
                        .chatId(message.getChatId().toString())
                        .build());
                case "Получить совет" -> {
                    List<String> tasteNames = Stream.of(Taste.values())
                            .map(Taste::getDescription)
                            .toList();
                    List<List<InlineKeyboardButton>> buttons = KeyboardMaker.getButtons(tasteNames);
                    execute(SendMessage.builder()
                            .text("Какие предпочтения по вкусу?")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
                }
                case "Скачать меню" -> {
                    SendDocument sendDocument = getDocument(message.getChatId());
                    execute(sendDocument);
                }
                case "Связаться" -> {
                    List<String> contactNames = Stream.of(Contact.values())
                            .map(Contact::getDescription)
                            .toList();
                    List<List<InlineKeyboardButton>> buttons = KeyboardMaker.getButtons(contactNames);
                    execute(SendMessage.builder()
                            .text("Где вам удобнее связаться?")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
                }
                case "Помощь" -> execute(SendMessage.builder()
                        .text("Привет! Это бот, который поможет тебе выбрать вкусный кальян в Эксперт Баре! " +
                                "Используй клавиатуру, чтобы начать работу")
                        .chatId(message.getChatId().toString())
                        .build());
                default -> execute(SendMessage.builder()
                        .text("Такая команда не поддерживается")
                        .chatId(message.getChatId().toString())
                        .build());
            }
        }
    }

    private SendMessage getMessage(long chatId, String advice) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(advice)
                .build();
    }

    private SendDocument getDocument(long chatId) {
        SendDocument sendDocumentRequest = new SendDocument();
        InputFile inputFile = new InputFile(menu);
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(inputFile);
        return sendDocumentRequest;
    }

    @SneakyThrows
    private void fillWaiting(long chatId, List<String> greetings) {
        Random random = new Random();
        int size = greetings.size();
        execute(getMessage(chatId, greetings.get(random.nextInt(size))));
        TimeUnit.SECONDS.sleep(1);
        int additionalSize = Greetings.additional.size();
        execute(getMessage(chatId, Greetings.additional.get(random.nextInt(additionalSize))));
        TimeUnit.SECONDS.sleep(1);
        execute(getMessage(chatId, "Нашел! Тебе подойдет:"));
        TimeUnit.SECONDS.sleep(1);
    }
}