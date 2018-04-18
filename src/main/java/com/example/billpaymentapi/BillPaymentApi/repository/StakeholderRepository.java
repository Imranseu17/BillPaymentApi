package com.example.billpaymentapi.BillPaymentApi.repository;

import com.example.billpaymentapi.BillPaymentApi.model.StakeHolderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StakeholderRepository extends JpaRepository<StakeHolderInfo,Integer> {
}
