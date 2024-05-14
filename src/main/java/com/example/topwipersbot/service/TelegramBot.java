package com.example.topwipersbot.service;

import com.example.topwipersbot.ButtonsUtil;
import com.example.topwipersbot.entity.User;
import com.example.topwipersbot.repository.CityRepository;
import com.example.topwipersbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.LoginUrl;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private static final String BOT_USERNAME = "TOP_Wipers_bot";
    private static final String BOT_TOKEN = "7087182366:AAElOZqnrJHgfpDXFa1aK9_g-8abndXE7Wg";
    List<BotCommand> botCommandList = new ArrayList<>();
    private static final String HELP_TEXT = "This bot can demonstration Spring function\n\n" + "You can execute commands from below\n\n" + "Type /start to see welcome message\n" + "Type /help  to open help list\n" + "Type /settings to open settings";
    private static int ind = 0;
    @Autowired
    private CityRepository cityRepo;
    @Autowired
    private UserRepository userRepo;

    // constructor
    public TelegramBot() {
        botCommandList.add(new BotCommand("/start", "start"));
        botCommandList.add(new BotCommand("/settings", "settings"));
        botCommandList.add(new BotCommand("/help", "help"));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message msg = update.getMessage();
            Long chatId = msg.getChatId();
            //command operations
            if (update.getMessage().isCommand()) {

                switch (msg.getText()) {
                    case "/start" -> {
                        SendMessage message = registerUser(msg);
                        message.setReplyMarkup(startKeyboard());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "/help" -> {
                        sendMessage(chatId, HELP_TEXT);
                    }
                    case "/settings" -> {

                    }
                    default -> sendMessage(chatId, "Command not found");

                }

            }
        }
        //callBack operations
        else if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            String callbackData = callback.getData();
            Integer callbackMsgId = callback.getMessage().getMessageId();
            Long callbackChatId = callback.getMessage().getChatId();
            var lang = callback.getFrom().getLanguageCode();

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(callbackChatId);
            editMessageText.setMessageId(callbackMsgId);
            editMessageText.setParseMode("HTML");

            switch (lang) {
                case "en" -> lang = "английский";
                case "ru" -> lang = "русский";
                case "es" -> lang = "испанасий";
                case "de" -> lang = "немецкий";
            }
            switch (callbackData) {
                case "MAIN" -> {
                    editMessageText.setText("""
                            Добро пожаловать в каталог щёток
                            стеклоочистителей TopWipers

                            <a href="https://goggle.com">Перейти на сайт</a>
                             """);
                    editMessageText.setReplyMarkup(startKeyboard());
                }
                case "CHOOSE_MARKS" -> {
                    editMessageText.setText("choose marks btn pressed");
                }
                case "SETTINGS" -> {
                    editMessageText.setText("Настройки\n\nТекущий язык: <b>" + lang + "</b>\n\nТекущий регион");
                    editMessageText.setReplyMarkup(settingKeyboard());
                }
                case "CHANGE_LANG" -> {
                    editMessageText.setText("Выберите язык\n\nТекущий язык: <b>" + lang + "</b>\n\n");
                    editMessageText.setReplyMarkup(languageKeyboard());
                }
                case "CHANGE_REGION" -> {
                    ind = 0;
                    editMessageText.setText("""
                            Выберите регион

                            Текущий регион: <b>AAAA</b>
                                                        
                            """);
                    editMessageText.setReplyMarkup(cityKeyboard());
                }
                case "Амстердам" -> {
                    User user = userRepo.findById(callback.getFrom().getId()).get();
                    user.setCity(callbackData);
                    userRepo.save(user);
                }
                case "NEXT" -> {
                    editMessageText.setText("Выберите регион\n\n Текущий регион:<b> Россия </b>\n\n");
                    editMessageText.setReplyMarkup(cityKeyboard());
                }
                case "BACK" -> {
                    switch (ind / 12) {
                        case 2 -> ind = 0;
                        case 3 -> ind = 12;
                        case 4 -> ind = 24;
                    }
                    editMessageText.setText("Выберите регион\n\n Текущий регион:<b> Россия </b>\n\n");
                    editMessageText.setReplyMarkup(cityKeyboard());
                }
                case "RU" -> {
                    callback.getFrom().setLanguageCode("ru");
                    editMessageText.setText("Язык успешно изменён!\n\nТекущий язык: <b>" + lang + "</b>\n");
                    editMessageText.setReplyMarkup(backToMainKeyboard());
                }
                case "EN" -> {
                    callback.getFrom().setLanguageCode("en");
                    editMessageText.setText("Язык успешно изменён!\n\nТекущий язык: <b>" + lang + "</b>\n");
                    editMessageText.setReplyMarkup(backToMainKeyboard());
                }
                case "DE" -> {
                    callback.getFrom().setLanguageCode("de");
                    editMessageText.setText("Язык успешно изменён!\n\nТекущий язык: <b>" + lang + "</b>\n");
                    editMessageText.setReplyMarkup(backToMainKeyboard());
                }
                case "ES" -> {
                    callback.getFrom().setLanguageCode("es");
                    editMessageText.setText("Язык успешно изменён!\n\nТекущий язык: <b>" + lang + "</b>\n");
                    editMessageText.setReplyMarkup(backToMainKeyboard());
                }
                default -> editMessageText.setText(callbackData + " callbackdata not supported");
            }
            try {
                execute(editMessageText);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //************ methods *************

    private InlineKeyboardMarkup setKeyboards(List<String> list) {
        if (ind < 0) ind = 0;
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < 12; i = i + 2) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                if (ind > list.size() - 1) break;
                var btn = ButtonsUtil.setButton(list.get(ind), list.get(ind));
                row.add(btn);
                ind++;
            }
            rows.add(row);
        }
        if (ind == 12) {
            var nextBtn = ButtonsUtil.setButton("Вперёд->", "NEXT");
            var rowGoTo = ButtonsUtil.setRow(nextBtn);
            rows.add(rowGoTo);
        } else if (ind < list.size()) {
            var previousBtn = ButtonsUtil.setButton("<-Назад", "BACK");
            var nextBtn = ButtonsUtil.setButton("Вперёд->", "NEXT");
            var rowGoTo = ButtonsUtil.setRow(previousBtn, nextBtn);
            rows.add(rowGoTo);
        } else if (ind == list.size()) {
            var previousBtn = ButtonsUtil.setButton("<-Назад", "BACK");
            var rowGoTo = ButtonsUtil.setRow(previousBtn);
            rows.add(rowGoTo);
        }

        var backBtn = ButtonsUtil.setButton("Назад к настройкам", "SETTINGS");
        var rowBack = ButtonsUtil.setRow(backBtn);
        rows.add(rowBack);
        return ButtonsUtil.setInlineMarkup(rows);
    }


    private SendMessage sendMessage(Long userid, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(userid);
        message.setText(text);
        message.setParseMode("HTML");
        return message;
    }

    public InlineKeyboardMarkup startKeyboard() {
        var btn1 = ButtonsUtil.setButton("Выбрать марку автомобиля", "CHOOSE_MARKS");
        btn1.setSwitchInlineQuery("hello");
        var btn2 = ButtonsUtil.setButton("⚙️ Настройки", "SETTINGS");
        var row1 = ButtonsUtil.setRow(btn1);
        var row2 = ButtonsUtil.setRow(btn2);
        var rows = ButtonsUtil.setRows(row1, row2);
        return ButtonsUtil.setInlineMarkup(rows);
    }

    private InlineKeyboardMarkup backToMainKeyboard() {
        var btn = ButtonsUtil.setButton("\uD83C\uDFE0 Вернуться в начало", "MAIN");
        var row = ButtonsUtil.setRow(btn);
        var rows = ButtonsUtil.setRows(row);
        return ButtonsUtil.setInlineMarkup(rows);
    }

    public InlineKeyboardMarkup settingKeyboard() {
        var btn1 = ButtonsUtil.setButton("Изменить язык", "CHANGE_LANG");
        var btn2 = ButtonsUtil.setButton("Изменить регион", "CHANGE_REGION");
        var btn3 = ButtonsUtil.setButton("\uD83C\uDFE0 Вернуться назад", "MAIN");
        var row1 = ButtonsUtil.setRow(btn1, btn2);
        var row2 = ButtonsUtil.setRow(btn3);
        var rows = ButtonsUtil.setRows(row1, row2);
        return ButtonsUtil.setInlineMarkup(rows);
    }

    public InlineKeyboardMarkup languageKeyboard() {
        var btn1 = ButtonsUtil.setButton("Русский", "RU");
        var btn2 = ButtonsUtil.setButton("Английский", "EN");
        var btn3 = ButtonsUtil.setButton("Немецкий", "DE");
        var btn4 = ButtonsUtil.setButton("Испанский", "ES");
        var btn5 = ButtonsUtil.setButton("⚙️ Назад к настройкам", "SETTINS");
        var row1 = ButtonsUtil.setRow(btn1, btn2);
        var row2 = ButtonsUtil.setRow(btn3, btn4);
        var row3 = ButtonsUtil.setRow(btn5);
        var rows = ButtonsUtil.setRows(row1, row2, row3);
        return ButtonsUtil.setInlineMarkup(rows);
    }

    private InlineKeyboardMarkup cityKeyboard() {
        return setKeyboards(cityRepo.findAllName());
    }

    private SendMessage registerUser(Message message) {
        if (userRepo.findById(message.getChatId()).isEmpty()) {
            User user = new User();
            user.setUserId(message.getChatId());
            user.setFirstname(message.getFrom().getFirstName());
            user.setSurname(message.getFrom().getLastName());
            user.setUsername(message.getFrom().getUserName());
            user.setCity("Россия");
            user.setRegisteredAt(new TimeStamp());
            userRepo.save(user);
            return sendMessage(message.getChatId(), """
                    Добро пожаловать в каталог щёток
                    стеклоочистителей TopWipers

                    <a href="https://goggle.com">Перейти на сайт</a>
                     """);
        }
        return sendMessage(message.getChatId(), """
                Рад вас снова видит в нашем каталоге щёток
                стеклоочистителей TopWipers

                <a href="https://goggle.com">Перейти на сайт</a>
                 """);
    }

}
