package com.example.dailyexpenses;

public class Expense {
    private String category;
    private String amount;
    private String timestamp;

    public Expense() {} // Required for Firebase

    public Expense(String category, String amount, String timestamp) {
        this.category = category;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getCategory() { return category; }
    public String getAmount() { return amount; }
    public String getTimestamp() { return timestamp; }
}