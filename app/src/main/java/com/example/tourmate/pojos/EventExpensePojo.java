package com.example.tourmate.pojos;

public class EventExpensePojo {

    private String expenseId;
    private String eventId;
    private String expenseName;
    private int amount;
    private String e_catagories;
    private String expenseDateTime;

    public EventExpensePojo() {
    }

    public EventExpensePojo(String expenseId, String eventId, String expenseName, int amount, String e_catagories, String expenseDateTime) {
        this.expenseId = expenseId;
        this.eventId = eventId;
        this.expenseName = expenseName;
        this.amount = amount;
        this.e_catagories = e_catagories;
        this.expenseDateTime = expenseDateTime;
    }

    public String getE_catagories() {
        return e_catagories;
    }

    public void setE_catagories(String e_catagories) {
        this.e_catagories = e_catagories;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getExpenseDateTime() {
        return expenseDateTime;
    }

    public void setExpenseDateTime(String expenseDateTime) {
        this.expenseDateTime = expenseDateTime;
    }
}
