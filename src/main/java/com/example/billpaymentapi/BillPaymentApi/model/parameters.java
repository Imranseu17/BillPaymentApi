package com.example.billpaymentapi.BillPaymentApi.model;

import java.time.LocalDateTime;
import java.util.Date;

public class parameters {

    private String billNumber;

    private Float billAmount;
    private String tranID;

    private String customerNumber;
    private String cancelRemarks;
    private String bankTranxnID;
    private Date dateTime;
    private String type;

    public parameters() {
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Float getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Float billAmount) {
        this.billAmount = billAmount;
    }

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    public void setCancelRemarks(String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public String getBankTranxnID() {
        return bankTranxnID;
    }

    public void setBankTranxnID(String bankTranxnID) {
        this.bankTranxnID = bankTranxnID;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
