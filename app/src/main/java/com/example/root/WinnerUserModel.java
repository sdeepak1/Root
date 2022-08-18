package com.example.root;

public class WinnerUserModel {
    String rank,name,url,uid,amount,gameId;

    public WinnerUserModel(String rank, String name, String url, String uid, String amount, String gameId) {
        this.rank = rank;
        this.name = name;
        this.url = url;
        this.uid = uid;
        this.amount = amount;
        this.gameId = gameId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
