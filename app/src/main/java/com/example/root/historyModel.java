package com.example.root;

public class historyModel {
    String textMoney,textData,textId,textDate,textTime;
    Boolean value;

    public historyModel(String textMoney, String textData, String textId, String textDate, String textTime,Boolean value) {
        this.textMoney = textMoney;
        this.textData = textData;
        this.textId = textId;
        this.textDate = textDate;
        this.textTime = textTime;
        this.value=value;
    }

    public String getTextMoney() {
        return textMoney;
    }


    public String getTextData() {
        return textData;
    }

    public String getTextId() {
        return textId;
    }


    public String getTextDate() {
        return textDate;
    }


    public String getTextTime() {
        return textTime;
    }


    public Boolean getValue() {
        return value;
    }

}
