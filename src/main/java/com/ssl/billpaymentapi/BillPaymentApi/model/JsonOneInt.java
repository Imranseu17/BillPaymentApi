package com.ssl.billpaymentapi.BillPaymentApi.model;

public class JsonOneInt {

    private  int id;

    public JsonOneInt() {
    }

    public JsonOneInt(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "JsonOneInt{" +
                "id=" + id +
                '}';
    }
}
