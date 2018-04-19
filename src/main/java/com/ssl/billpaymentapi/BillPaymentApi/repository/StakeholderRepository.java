package com.ssl.billpaymentapi.BillPaymentApi.repository;


import com.ssl.billpaymentapi.BillPaymentApi.model.StakeHolderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StakeholderRepository extends JpaRepository<StakeHolderInfo,Integer> {
}
