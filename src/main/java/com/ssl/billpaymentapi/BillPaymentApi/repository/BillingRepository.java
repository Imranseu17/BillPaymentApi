package com.ssl.billpaymentapi.BillPaymentApi.repository;



import com.ssl.billpaymentapi.BillPaymentApi.model.BillingInformation;
import com.ssl.billpaymentapi.BillPaymentApi.model.BillingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface BillingRepository extends JpaRepository<BillingInformation, Integer> {

    BillingInformation findByBillNumber(String bill_number);

    List<BillingInformation> findAllByCustomerNumber(String customer_number);

    List<BillingInformation> findAllByIssueDate(Date date);
    List<BillingInformation> findAllByIssueDate(LocalDateTime date);

    List<BillingInformation> findAllByIssueDateAndBillStatus(Date date, BillingStatus billingStatus);

    BillingInformation findByBankTranxnIDAndTranID(String bankTranxnID,String tranID);






}
