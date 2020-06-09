package com.example.tourmate.pojos;

public class ExpenseAmountData {
    public int foodExpense;
    public int transportExpense;
    public int hotelExpense;
    public int otherExpenese;

    public ExpenseAmountData(int foodExpense, int transportExpense, int hotelExpense, int otherExpenese) {
        this.foodExpense = foodExpense;
        this.transportExpense = transportExpense;
        this.hotelExpense = hotelExpense;
        this.otherExpenese = otherExpenese;
    }

    public int getFoodExpense() {
        return foodExpense;
    }

    public void setFoodExpense(int foodExpense) {
        this.foodExpense = foodExpense;
    }

    public int getTransportExpense() {
        return transportExpense;
    }

    public void setTransportExpense(int transportExpense) {
        this.transportExpense = transportExpense;
    }

    public int getHotelExpense() {
        return hotelExpense;
    }

    public void setHotelExpense(int hotelExpense) {
        this.hotelExpense = hotelExpense;
    }

    public int getOtherExpenese() {
        return otherExpenese;
    }

    public void setOtherExpenese(int otherExpenese) {
        this.otherExpenese = otherExpenese;
    }
}
