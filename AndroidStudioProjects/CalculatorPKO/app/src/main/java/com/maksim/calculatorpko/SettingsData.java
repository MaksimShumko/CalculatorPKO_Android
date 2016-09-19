package com.maksim.calculatorpko;

/**
 * Created by Maksim on 2016-09-19.
 */
public class SettingsData {
    private String operation;
    private int interval;
    private int amount;
    private String curr;

    public static final int ONLY_NEGATIVE = 0;
    public static final int ALL = 1;
    public static final int ONLY_POSITIVE = 2;

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int WEEK = 2;
    public static final int DAY = 3;

    public SettingsData() {
        this.operation = "Płatność kartą";
        this.interval = MONTH;
        this.amount = ONLY_NEGATIVE;
        this.curr = "PLN";
    }

    public String getOperation() {
        return operation;
    }
    public int getInterval() {
        return interval;
    }
    public int getAmount() {
        return amount;
    }
    public String getCurr() {
        return curr;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setCurr(String curr) {
        this.curr = curr;
    }
}
