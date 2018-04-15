package com.example.billpaymentapi.BillPaymentApi.repository;

import com.example.billpaymentapi.BillPaymentApi.model.BillingInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BillingRepository extends JpaRepository<BillingInformation, Integer> {

    BillingInformation findByBillNumber(String bill_number);

    List<BillingInformation> findAllByCustomerNumber(String customer_number);

    List<BillingInformation> findAllByIssueDate(Date date);




}
