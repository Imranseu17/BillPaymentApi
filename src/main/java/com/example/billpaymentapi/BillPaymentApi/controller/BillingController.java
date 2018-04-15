package com.example.billpaymentapi.BillPaymentApi.controller;



import com.example.billpaymentapi.BillPaymentApi.model.*;
import com.example.billpaymentapi.BillPaymentApi.repository.BillingRepository;
import com.example.billpaymentapi.BillPaymentApi.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api")
public class BillingController {




    @Autowired
    BillingRepository billingRepository;

    @Autowired
    UsersRepository usersRepository;




    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @PostMapping(value = "/secured/singleBillInformation/")
    public Object findOneBillInformation(
            @RequestParam("billNumber") String billNumber,
            @RequestParam("username") String username, @RequestParam("password") String password){



        Users  users = usersRepository.findByUserName(username);
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(password,users.getPassword())){
                    return billingRepository.findByBillNumber(billNumber);
                }
            }
        }


        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;
    }




    @PostMapping("/secured/updatePaymentBillInformation/")
    public JsonType PayBill(
                            @RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam("billNumber") String billNumber,
                            @RequestParam("amount") Float amount) {

        BillingInformation billingInformation = billingRepository.findByBillNumber(billNumber);
        Float paidAmount = billingInformation.getPaid_amount();


        try {

            Float newAmount = paidAmount + amount;
            Float bill_amount = billingInformation.getBill_amount();
            Float vat_amount = billingInformation.getVat_amount();
            Float totalAmount = bill_amount+vat_amount;
            Float due_amount = totalAmount - newAmount;



            if (newAmount >= totalAmount)
                billingInformation.setBillingStatus(BillingStatus.paid);
            else if (newAmount == 0)
                billingInformation.setBillingStatus(BillingStatus.pending);
            else
                billingInformation.setBillingStatus(BillingStatus.due);

            billingInformation.setPaid_amount(newAmount);
            billingInformation.setTotal_amount(totalAmount);
            billingInformation.setDue_amount(due_amount);
            billingInformation.setPay_date(Date.valueOf(LocalDate.now()));
            billingInformation.setPaid_by(username);
            billingRepository.save(billingInformation);

        } catch (Exception e) {
            e.printStackTrace();
            JsonType jsonType = new JsonType("Unsuccessful",
                    billingInformation.getBillingStatus().getMeaning());
            return jsonType;
        }

        JsonType jsonType = new JsonType("Successful",
                billingInformation.getBillingStatus().getMeaning());


        Users  users = usersRepository.findByUserName(username);
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(password,users.getPassword())){
                    return jsonType;
                }
            }
        }

        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;
    }


    @PostMapping(value = "/secured/unpaidAllBillInformation/")
    public List<Object> findAllUnpaidBillInformation(@RequestParam("customerNumber")
                                                                      String customerNumber,
                                                                 @RequestParam("username") String username,
                                                                 @RequestParam("password") String password) {

        List<BillingInformation> billingInformationUnpaidList = new ArrayList<>();

        List<BillingInformation> billingInformationList = billingRepository.
                findAllByCustomerNumber(customerNumber);


        for (BillingInformation billInfo : billingInformationList) {

            if (billInfo.getBillingStatus().getMeaning().equals("pending")) {
                billingInformationUnpaidList.add(billInfo);
            }

            if (billInfo.getBillingStatus().getMeaning().equals("due")) {

                billingInformationUnpaidList.add(billInfo);
            }
        }



        Users  users = usersRepository.findByUserName(username);
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(password,users.getPassword())){
                    return Collections.singletonList(billingInformationUnpaidList);
                }
            }
        }

        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return Collections.singletonList(unSuccessJsonType);
    }

    @PostMapping("/secured/cancelBillInformation/")
    public JsonType cancelBillInformation(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("billNumber") String billNumber,
                                          @RequestParam(value = "cancelRemarks",
                                                  required = false)
                                                  String cancelRemarks
                                           ) {

        BillingInformation billingInformation = billingRepository.findByBillNumber(billNumber);
        Float due_amount = billingInformation.getTotal_amount();

        try {
            billingInformation.setRemarks(cancelRemarks);
            billingInformation.setPaid_amount(0);
            billingInformation.setBillingStatus(BillingStatus.cancelled);
            billingInformation.setCancel_date(Date.valueOf(LocalDate.now()));
            billingInformation.setDue_amount(due_amount);
            billingInformation.setCancelled_by(username);

            if (billingInformation.getPay_date().equals(Date.valueOf(LocalDate.now())))
                billingRepository.save(billingInformation);

            else {
                JsonType jsonMessage = new JsonType("Date Expired",
                        "Unable cancel payment");
                return jsonMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();

            JsonType jsonType = new JsonType("Unsuccessful",
                    billingInformation.getBillingStatus().getMeaning());
            return jsonType;
        }

        JsonType jsonType = new JsonType("Successful",
                billingInformation.getBillingStatus().getMeaning());




        Users  users = usersRepository.findByUserName(username);
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(password,users.getPassword())){
                    return jsonType;
                }
            }
        }

        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;
    }

    @PostMapping(value = "/secured/currentDateBillInformation/")
    public List<Object> findAllCurrentDateBillInformation(
            @RequestParam("username") String username, @RequestParam("password") String password){


        Users  users = usersRepository.findByUserName(username);
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(password,users.getPassword())){
                    return Collections.singletonList(billingRepository.findAllByIssueDate(Date.valueOf(LocalDate.now())));
                }
            }
        }


        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return Collections.singletonList(unSuccessJsonType);
    }



}



