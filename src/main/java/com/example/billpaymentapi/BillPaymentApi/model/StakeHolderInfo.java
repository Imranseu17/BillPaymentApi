package com.example.billpaymentapi.BillPaymentApi.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stakeholder_info")
public class StakeHolderInfo {
    @Id
    @GeneratedValue
    private  int id;

    private String name;
    private String address;
    private String contactPersonName;
    private String contactPersonMobile;
    private  int status;

    public StakeHolderInfo() {
    }

    public StakeHolderInfo(String name, String address, String contactPersonName, String contactPersonMobile, int status) {
        this.name = name;
        this.address = address;
        this.contactPersonName = contactPersonName;
        this.contactPersonMobile = contactPersonMobile;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonMobile() {
        return contactPersonMobile;
    }

    public void setContactPersonMobile(String contactPersonMobile) {
        this.contactPersonMobile = contactPersonMobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StakeHolderInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactPersonName='" + contactPersonName + '\'' +
                ", contactPersonMobile='" + contactPersonMobile + '\'' +
                ", status=" + status +
                '}';
    }
}
