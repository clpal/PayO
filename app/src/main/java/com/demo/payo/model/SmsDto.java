package com.demo.payo.model;

public class SmsDto {
    private Float Amount;
    private int transactionType;
    private String date;
    private String msgData;




    public SmsDto(){

    }
    public SmsDto(String _msgData, float amount, String date, int transactionType) {
        this.msgData = _msgData;
        this.Amount = amount;
        this.date = date;
        this.transactionType = transactionType;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return msgData;
    }


    public double getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        transactionType = transactionType;
    }


}
