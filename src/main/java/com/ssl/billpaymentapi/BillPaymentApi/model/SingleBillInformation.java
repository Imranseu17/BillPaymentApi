package com.ssl.billpaymentapi.BillPaymentApi.model;

import java.time.LocalDateTime;

public class SingleBillInformation {

    private  String message ;
    private  int id;
    private String  billNumber;
    private  float billAmount;
    private  String billStatus;
    private LocalDateTime cancelDate;
    private String cancelBy;
    private  String customerNumber;
    private  float dueAmount;
    private  float paidAmount;
    private  float totalAmount;
    private String paidBy;
    private  float vatAmount;
    private  LocalDateTime payDate;
    private  String ackStatus;
    private  String bank_tranxn_id;
    private  String tran_id;

    public SingleBillInformation() {
    }

    public SingleBillInformation(String message, int id, String billNumber, float billAmount, String billStatus, LocalDateTime cancelDate, String cancelBy, String customerNumber, float dueAmount, float paidAmount, float totalAmount, String paidBy, float vatAmount, LocalDateTime payDate, String ackStatus, String bank_tranxn_id, String tran_id) {
        this.message = message;
        this.id = id;
        this.billNumber = billNumber;
        this.billAmount = billAmount;
        this.billStatus = billStatus;
        this.cancelDate = cancelDate;
        this.cancelBy = cancelBy;
        this.customerNumber = customerNumber;
        this.dueAmount = dueAmount;
        this.paidAmount = paidAmount;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.vatAmount = vatAmount;
        this.payDate = payDate;
        this.ackStatus = ackStatus;
        this.bank_tranxn_id = bank_tranxn_id;
        this.tran_id = tran_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public float getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(float billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public float getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(float dueAmount) {
        this.dueAmount = dueAmount;
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public float getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public LocalDateTime getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDateTime payDate) {
        this.payDate = payDate;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public String getBank_tranxn_id() {
        return bank_tranxn_id;
    }

    public void setBank_tranxn_id(String bank_tranxn_id) {
        this.bank_tranxn_id = bank_tranxn_id;
    }

    public String getTran_id() {
        return tran_id;
    }

    public void setTran_id(String tran_id) {
        this.tran_id = tran_id;
    }

    @Override
    public String toString() {
        return "SingleBillInformation{" +
                "message='" + message + '\'' +
                ", id=" + id +
                ", billNumber='" + billNumber + '\'' +
                ", billAmount=" + billAmount +
                ", billStatus='" + billStatus + '\'' +
                ", cancelDate=" + cancelDate +
                ", cancelBy='" + cancelBy + '\'' +
                ", customerNumber='" + customerNumber + '\'' +
                ", dueAmount=" + dueAmount +
                ", paidAmount=" + paidAmount +
                ", totalAmount=" + totalAmount +
                ", paidBy='" + paidBy + '\'' +
                ", vatAmount=" + vatAmount +
                ", payDate=" + payDate +
                ", ackStatus='" + ackStatus + '\'' +
                ", bank_tranxn_id='" + bank_tranxn_id + '\'' +
                ", tran_id='" + tran_id + '\'' +
                '}';
    }
}
