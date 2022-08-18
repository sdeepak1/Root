package com.example.root;

public class showAmountModel {
    String rank,amount;

    public showAmountModel(String rank, String amount) {
        this.rank = rank;
        this.amount = amount;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
