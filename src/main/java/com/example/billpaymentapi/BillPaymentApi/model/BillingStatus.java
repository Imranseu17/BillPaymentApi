package com.example.billpaymentapi.BillPaymentApi.model;

import javax.persistence.Table;

@Table(name = "bill_status_info")
public enum BillingStatus {

    none(0, "none"),
    pending(1, "pending"),
    paid(2, "paid"),
    due(3, "due"),
    cancelled(4, "cancelled"),
    Failed(5,"Failed");


    String meaning;
    int key;

    BillingStatus(int key, String meaning) {
        this.meaning = meaning;
        this.key = key;
    }

    BillingStatus() {
    }

    public String getMeaning() {
        return meaning;
    }

    public int getKey() {
        return key;
    }


    @Override
    public String toString() {
        return "BillingStatus{" +
                "meaning='" + meaning + '\'' +
                ", key=" + key +
                '}';
    }
}
