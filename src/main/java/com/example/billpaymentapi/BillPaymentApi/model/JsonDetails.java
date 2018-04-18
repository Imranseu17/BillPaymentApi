package com.example.billpaymentapi.BillPaymentApi.model;

import java.util.List;

public class JsonDetails  {

    private  int totalCount;
    private Float totalAmount;
    private  int totalSuccessCount;
    private Float totalSuccessAmount;
    private  int totalFailedCount;
    private Float totalFailedAmount;

    private List<BillingInformation> billingInformationList;

    public JsonDetails(int totalCount, Float totalAmount, int totalSuccessCount, Float totalSuccessAmount, int totalFailedCount, Float totalFailedAmount, List<BillingInformation> billingInformationList) {
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
        this.totalSuccessCount = totalSuccessCount;
        this.totalSuccessAmount = totalSuccessAmount;
        this.totalFailedCount = totalFailedCount;
        this.totalFailedAmount = totalFailedAmount;
        this.billingInformationList = billingInformationList;
    }

    public JsonDetails() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalSuccessCount() {
        return totalSuccessCount;
    }

    public void setTotalSuccessCount(int totalSuccessCount) {
        this.totalSuccessCount = totalSuccessCount;
    }

    public Float getTotalSuccessAmount() {
        return totalSuccessAmount;
    }

    public void setTotalSuccessAmount(Float totalSuccessAmount) {
        this.totalSuccessAmount = totalSuccessAmount;
    }

    public int getTotalFailedCount() {
        return totalFailedCount;
    }

    public void setTotalFailedCount(int totalFailedCount) {
        this.totalFailedCount = totalFailedCount;
    }

    public Float getTotalFailedAmount() {
        return totalFailedAmount;
    }

    public void setTotalFailedAmount(Float totalFailedAmount) {
        this.totalFailedAmount = totalFailedAmount;
    }

    public List<BillingInformation> getBillingInformationList() {
        return billingInformationList;
    }

    public void setBillingInformationList(List<BillingInformation> billingInformationList) {
        this.billingInformationList = billingInformationList;
    }

    @Override
    public String toString() {
        return "JsonDetails{" +
                "totalCount=" + totalCount +
                ", totalAmount=" + totalAmount +
                ", totalSuccessCount=" + totalSuccessCount +
                ", totalSuccessAmount=" + totalSuccessAmount +
                ", totalFailedCount=" + totalFailedCount +
                ", totalFailedAmount=" + totalFailedAmount +
                ", billingInformationList=" + billingInformationList +
                '}';
    }
}
