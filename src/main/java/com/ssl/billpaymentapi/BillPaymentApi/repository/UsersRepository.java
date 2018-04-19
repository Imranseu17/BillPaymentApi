package com.ssl.billpaymentapi.BillPaymentApi.repository;


import com.ssl.billpaymentapi.BillPaymentApi.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {


    Users findByUserName(String name);


}
