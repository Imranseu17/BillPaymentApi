package com.ssl.billpaymentapi.BillPaymentApi.model;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill_info")
public class BillingInformation {
    @Id
    @GeneratedValue
    private int id;
    private String customerNumber;
    private String billNumber;
    @Column(name = "utility_trnxn_id")
    private String utilityTrnxnID;
    @Column(name = "bank_tranxn_id")
    private String bankTranxnID;
    private int billType;
    private Date issueDate;
    private float billAmount;
    private float vatAmount;
    private float totalAmount;
    private float paidAmount;
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime payDate;
    private String paidBy;
    private float dueAmount;
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime due_date;
    @Column(name = "tran_id")
    private String tranID;
    private String ackStatus;
    @Enumerated(EnumType.ORDINAL)
    private BillingStatus billStatus;

    private String cancelled_by;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime cancelDate;
    private String remarks;

    @OneToOne
    @JoinColumn(name = "stakeholderID")
    private StakeHolderInfo stakeHolderInfo;



    public BillingInformation(String customerNumber, String billNumber, String utilityTrnxnID, String bankTranxnID, int billType, Date issueDate, float billAmount, float vatAmount, float totalAmount, float paidAmount, LocalDateTime payDate, String paidBy, float dueAmount, LocalDateTime due_date, String tranID, String ackStatus, BillingStatus billingStatus, String cancelled_by, LocalDateTime cancelDate, String remarks) {
        this.customerNumber = customerNumber;
        this.billNumber = billNumber;
        this.utilityTrnxnID = utilityTrnxnID;
        this.bankTranxnID = bankTranxnID;
        this.billType = billType;
        this.issueDate = issueDate;
        this.billAmount = billAmount;
        this.vatAmount = vatAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.payDate = payDate;
        this.paidBy = paidBy;
        this.dueAmount = dueAmount;
        this.due_date = due_date;
        this.tranID = tranID;
        this.ackStatus = ackStatus;
        this.billStatus = billingStatus;
        this.cancelled_by = cancelled_by;
        this.cancelDate = cancelDate;
        this.remarks = remarks;
    }

    public BillingInformation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public BillingStatus getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(BillingStatus billStatus) {
        this.billStatus = billStatus;
    }

    public StakeHolderInfo getStakeHolderInfo() {
        return stakeHolderInfo;
    }

    public void setStakeHolderInfo(StakeHolderInfo stakeHolderInfo) {
        this.stakeHolderInfo = stakeHolderInfo;
    }

    public String getUtilityTrnxnID() {
        return utilityTrnxnID;
    }

    public void setUtilityTrnxnID(String utilityTrnxnID) {
        this.utilityTrnxnID = utilityTrnxnID;
    }

    public String getBankTranxnID() {
        return bankTranxnID;
    }

    public void setBankTranxnID(String bankTranxnID) {
        this.bankTranxnID = bankTranxnID;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public float getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(float billAmount) {
        this.billAmount = billAmount;
    }

    public float getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public LocalDateTime getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDateTime payDate) {
        this.payDate = payDate;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public float getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(float dueAmount) {
        this.dueAmount = dueAmount;
    }

    public LocalDateTime getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDateTime due_date) {
        this.due_date = due_date;
    }

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public BillingStatus getBillingStatus() {
        return billStatus;
    }

    public void setBillingStatus(BillingStatus billingStatus) {
        this.billStatus = billingStatus;
    }

    public String getCancelled_by() {
        return cancelled_by;
    }

    public void setCancelled_by(String cancelled_by) {
        this.cancelled_by = cancelled_by;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "BillingInformation{" +
                "id=" + id +
                ", customerNumber='" + customerNumber + '\'' +
                ", billNumber='" + billNumber + '\'' +
                ", utilityTrnxnID='" + utilityTrnxnID + '\'' +
                ", bankTranxnID='" + bankTranxnID + '\'' +
                ", billType=" + billType +
                ", issueDate=" + issueDate +
                ", billAmount=" + billAmount +
                ", vatAmount=" + vatAmount +
                ", totalAmount=" + totalAmount +
                ", paidAmount=" + paidAmount +
                ", payDate=" + payDate +
                ", paidBy='" + paidBy + '\'' +
                ", dueAmount=" + dueAmount +
                ", due_date=" + due_date +
                ", tranID='" + tranID + '\'' +
                ", ackStatus='" + ackStatus + '\'' +
                ", billingStatus=" + billStatus +
                ", cancelled_by='" + cancelled_by + '\'' +
                ", cancelDate=" + cancelDate +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
