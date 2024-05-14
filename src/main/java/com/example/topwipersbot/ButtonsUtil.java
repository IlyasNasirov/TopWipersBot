package com.example.topwipersbot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonsUtil {
    public static String[] cities={"1","2","3","4","5","6","7","8",
                                    "9","10","11","12","13","14","15","16","17","18",
                                    "19","20","21","22","23"};

    //create inline button
    public static InlineKeyboardButton setButton(String text, String callbackData){
        InlineKeyboardButton btn=new InlineKeyboardButton();
        btn.setText(text);
        btn.setCallbackData(callbackData);
        return btn;
    }
    //create inline row
    public static List<InlineKeyboardButton> setRow(InlineKeyboardButton... buttons){
        List<InlineKeyboardButton> row=new ArrayList<>();
        row.addAll(List.of(buttons));
        return row;
    }
    //create inline rows
    @SafeVarargs
    public static List<List<InlineKeyboardButton>> setRows(List<InlineKeyboardButton>... row){
        List<List<InlineKeyboardButton>> rows=new ArrayList<>();
        rows.addAll(Arrays.asList(row));
        return rows;
    }
    // create inline keyboard markup
    public static InlineKeyboardMarkup setInlineMarkup(List<List<InlineKeyboardButton>> rows){
        InlineKeyboardMarkup inlineKeyboardMarkup=new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
    //*****************************
    // create Reply keyboard button
    public static KeyboardButton setButton(String text){
        KeyboardButton button=new KeyboardButton();
        button.setText(text);
        return button;
    }

    // create reply row
    public static KeyboardRow setRow(String... setButtons){
        KeyboardRow row=new KeyboardRow();
        row.addAll(Arrays.asList(setButtons));
        return row;
    }
    //create reply rows
    public static List<KeyboardRow> setRows(KeyboardRow... setRow ){
        List<KeyboardRow> rows=new ArrayList<>();
        rows.addAll(List.of(setRow));
        return rows;
    }
    //create reply keyboard markup
    public static ReplyKeyboardMarkup setReplyMarkup(List<KeyboardRow> setRows){
        ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(setRows);
        return replyKeyboardMarkup;
    }


}
