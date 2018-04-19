package com.ssl.billpaymentapi.BillPaymentApi.model;

public class JsonOneData {

    private String status;

    public JsonOneData(String status) {
        this.status = status;
    }

    public JsonOneData() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "JsonOneData{" +
                "status='" + status + '\'' +
                '}';
    }
}
