package com.example.billpaymentapi.BillPaymentApi.model;

public class JsonSummary {

    private  int totalCount;
    private Float totalAmount;
    private  int totalSuccessCount;
    private Float totalSuccessAmount;
    private  int totalFailedCount;
    private Float totalFailedAmount;

    public JsonSummary(int totalCount, Float totalAmount, int totalSuccessCount, Float totalSuccessAmount, int totalFailedCount, Float totalFailedAmount) {
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
        this.totalSuccessCount = totalSuccessCount;
        this.totalSuccessAmount = totalSuccessAmount;
        this.totalFailedCount = totalFailedCount;
        this.totalFailedAmount = totalFailedAmount;
    }

    public JsonSummary() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalSuccessCount() {
        return totalSuccessCount;
    }

    public void setTotalSuccessCount(int totalSuccessCount) {
        this.totalSuccessCount = totalSuccessCount;
    }

    public int getTotalFailedCount() {
        return totalFailedCount;
    }

    public void setTotalFailedCount(int totalFailedCount) {
        this.totalFailedCount = totalFailedCount;
    }

    public Float getTotalSuccessAmount() {
        return totalSuccessAmount;
    }

    public void setTotalSuccessAmount(Float totalSuccessAmount) {
        this.totalSuccessAmount = totalSuccessAmount;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Float getTotalFailedAmount() {
        return totalFailedAmount;
    }

    public void setTotalFailedAmount(Float totalFailedAmount) {
        this.totalFailedAmount = totalFailedAmount;
    }

    @Override
    public String toString() {
        return "JsonSummary{" +
                "totalCount=" + totalCount +
                ", totalAmount=" + totalAmount +
                ", totalSuccessCount=" + totalSuccessCount +
                ", totalSuccessAmount=" + totalSuccessAmount +
                ", totalFailedCount=" + totalFailedCount +
                ", totalFailedAmount=" + totalFailedAmount +
                '}';
    }
}
