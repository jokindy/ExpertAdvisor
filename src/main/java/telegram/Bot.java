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
import java.util.stream.Collectors;
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
            case "Сладенькое":
                int size = Mix.sweetMixes.size();
                String advice = Mix.sweetMixes.get(random.nextInt(size));
                fillWaiting(chatId, Greetings.sweetGreetings);
                execute(getMessage(chatId, advice));
                break;
            case "Кисленькое":
                size = Mix.sourMixes.size();
                advice = Mix.sourMixes.get(random.nextInt(size));
                fillWaiting(chatId, Greetings.sourGreetings);
                execute(getMessage(chatId, advice));
                break;
            case "Странное":
                size = Mix.strangeMixes.size();
                advice = Mix.strangeMixes.get(random.nextInt(size));
                fillWaiting(chatId, Greetings.strangeGreetings);
                execute(getMessage(chatId, advice));
                break;
            case "Двойное яблоко":
                execute(getMessage(chatId, Greetings.doubleAppleGreetings.get(random.nextInt(3))));
                TimeUnit.SECONDS.sleep(1);
                execute(getMessage(chatId, "Больной ублюдок..."));
                break;
            case "Вконтакте":
                execute(getMessage(chatId, "vk.com/expertbar"));
                break;
            case "Телеграм":
                execute(getMessage(chatId, "@ExpertBar"));
                break;
            case "Инстаграм":
                execute(getMessage(chatId, "instagram.com/expertbar"));
                break;
            case "Телефон":
                execute(getMessage(chatId, "+79219135164"));
                break;
        }
        TimeUnit.SECONDS.sleep(1);
        execute(getMessage(chatId, "Спасибо за использование! Выберете команду:"));
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.hasText()) {
            String command = message.getText();
            switch (command) {
                case "/start":
                    execute(SendMessage.builder()
                            .text("Привет! Я помогу тебе выбрать вкусный кальян в Эксперте! Используй клавиатуру, " +
                                    "чтобы начать работу")
                            .replyMarkup(KeyboardMaker.getMainMenuKeyboard())
                            .chatId(message.getChatId().toString())
                            .build());
                    break;
                case "Получить совет":
                    List<String> tasteNames = Stream.of(Taste.values())
                            .map(Taste::getDescription)
                            .collect(Collectors.toList());
                    List<List<InlineKeyboardButton>> buttons = KeyboardMaker.getButtons(tasteNames);
                    execute(SendMessage.builder()
                            .text("Какие предпочтения по вкусу?")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
                    break;
                case "Скачать меню":
                    SendDocument sendDocument = getDocument(message.getChatId());
                    execute(sendDocument);
                    break;
                case "Связаться":
                    List<String> contactNames = Stream.of(Contact.values())
                            .map(Contact::getDescription)
                            .collect(Collectors.toList());
                    buttons = KeyboardMaker.getButtons(contactNames);
                    execute(SendMessage.builder()
                            .text("Где вам удобнее связаться?")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
                    break;
                case "Помощь":
                    execute(SendMessage.builder()
                            .text("Привет! Это бот, который поможет тебе выбрать вкусный кальян в Эксперт Баре! " +
                                    "Используй клавиатуру, чтобы начать работу")
                            .chatId(message.getChatId().toString())
                            .build());
                    break;
                default:
                    execute(SendMessage.builder()
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