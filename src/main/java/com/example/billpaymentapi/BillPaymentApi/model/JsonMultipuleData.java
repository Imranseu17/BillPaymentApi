package com.example.billpaymentapi.BillPaymentApi.model;

public class JsonMultipuleData {

    private  String status;

    private String message;

    private  String bankTranID;

    private  String tranID;

    public JsonMultipuleData(String status, String message, String bankTranID, String tranID) {
        this.status = status;
        this.message = message;
        this.bankTranID = bankTranID;
        this.tranID = tranID;
    }

    public JsonMultipuleData() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBankTranID() {
        return bankTranID;
    }

    public void setBankTranID(String bankTranID) {
        this.bankTranID = bankTranID;
    }

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    @Override
    public String toString() {
        return "JsonMultipuleData{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", bankTranID='" + bankTranID + '\'' +
                ", tranID='" + tranID + '\'' +
                '}';
    }
}
