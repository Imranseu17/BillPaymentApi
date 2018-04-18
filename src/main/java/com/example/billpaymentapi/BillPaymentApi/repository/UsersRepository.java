package com.example.billpaymentapi.BillPaymentApi.repository;

import com.example.billpaymentapi.BillPaymentApi.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {


    Users findByUserName(String name);


}
