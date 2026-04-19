package com.furqan.financetracker;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int id;
    private String type;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate date;

    public Transaction(int id, String type, BigDecimal amount, String category, String description, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getDate() {
        return date;
    }
}