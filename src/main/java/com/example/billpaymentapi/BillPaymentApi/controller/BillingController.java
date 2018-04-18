package com.example.billpaymentapi.BillPaymentApi.controller;



import com.example.billpaymentapi.BillPaymentApi.model.*;
import com.example.billpaymentapi.BillPaymentApi.repository.BillingRepository;
import com.example.billpaymentapi.BillPaymentApi.repository.StakeholderRepository;
import com.example.billpaymentapi.BillPaymentApi.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    StakeholderRepository stakeholderRepository;




    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @PostMapping(value = "/secured/singleBillInformation/")
    public Object findOneBillInformation(
    @RequestBody JsonSend data){



        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())){
                    return billingRepository.findByBillNumber(data.getData().getParameters().getBillNumber());
                }
            }
        }


        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;
    }




    @PostMapping("/secured/updatePaymentBillInformation/")
    public Object PayBill(
            @RequestBody JsonSend data) {

        BillingInformation billingInformation = billingRepository.
                findByBillNumber(data.getData().getParameters().getBillNumber());
        Float paidAmount = billingInformation.getPaidAmount();
        String bankTranID = data.getData().getParameters().getTranID();
        String tranxID = billingInformation.getTranID();
        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        StakeHolderInfo stakeHolderInfo = stakeholderRepository.findOne(users.getStakeholderID().getId());

        try {

            Float newAmount = paidAmount + data.getData().getParameters().getBillAmount();
            Float bill_amount = billingInformation.getBillAmount();
            Float vat_amount = billingInformation.getVatAmount();
            Float totalAmount = bill_amount+vat_amount;
            Float due_amount = totalAmount - newAmount;



            if (newAmount >= totalAmount)
                billingInformation.setBillingStatus(BillingStatus.paid);
            else
                billingInformation.setBillingStatus(BillingStatus.pending);

            billingInformation.setPaidAmount(newAmount);
            billingInformation.setTotalAmount(totalAmount);
            billingInformation.setDueAmount(due_amount);
            billingInformation.setPayDate(LocalDateTime.now());
            billingInformation.setPaidBy(data.getData().getAccess_info().getUsername());
            billingInformation.setBankTranxnID(data.getData().getParameters().getTranID());
            billingInformation.setStakeHolderInfo(stakeHolderInfo);
            billingRepository.save(billingInformation);

        } catch (Exception e) {
            e.printStackTrace();
            JsonType jsonType = new JsonType("Unsuccessful",
                    billingInformation.getBillingStatus().getMeaning());
            return jsonType;
        }

        JsonMultipuleData jsonType = new JsonMultipuleData("Successful",
                billingInformation.getBillingStatus().getMeaning(),
                bankTranID,tranxID);



        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())){
                    return jsonType;
                }
            }
        }

        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;
    }


    @PostMapping(value = "/secured/unpaidAllBillInformation/")
    public List<Object> findAllUnpaidBillInformation(@RequestBody JsonSend data){

        List<BillingInformation> billingInformationUnpaidList = new ArrayList<>();

        List<BillingInformation> billingInformationList = billingRepository.
                findAllByCustomerNumber(data.getData().getParameters().getCustomerNumber());


        for (BillingInformation billInfo : billingInformationList) {

            if (billInfo.getBillingStatus().getMeaning().equals("pending")) {
                billingInformationUnpaidList.add(billInfo);
            }

            if (billInfo.getBillingStatus().getMeaning().equals("due")) {

                billingInformationUnpaidList.add(billInfo);
            }
        }



        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())){
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
            @RequestBody JsonSend data){

        BillingInformation billingInformation = billingRepository.
                findByBillNumber(data.getData().getParameters().getBillNumber());
        Float due_amount = billingInformation.getTotalAmount();

        try {
            billingInformation.setRemarks(data.getData().getParameters().getCancelRemarks());
            billingInformation.setPaidAmount(0);
            billingInformation.setBillingStatus(BillingStatus.cancelled);
            billingInformation.setCancelDate(LocalDateTime.now());
            billingInformation.setDueAmount(due_amount);
            billingInformation.setCancelled_by(data.getData().getAccess_info().getUsername());

            if (billingInformation.getPayDate().equals(Date.valueOf(LocalDate.now())))
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




        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())){
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
            @RequestBody JsonSend data){


        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())){
                    return Collections.singletonList(billingRepository.findAllByIssueDate(Date.valueOf(LocalDate.now())));
                }
            }
        }


        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return Collections.singletonList(unSuccessJsonType);
    }

    @PostMapping(value = "/secured/AcknowledgeApi/")
    public Object findAcKnowledgeStatus(
            @RequestBody JsonSend data ){


        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())){
                 BillingInformation billingInformation =
                         billingRepository.findByBankTranxnIDAndTranID(
                                 data.getData().getParameters().getBankTranxnID(),
                                 data.getData().getParameters().getTranID()
                         );
                 if(billingInformation == null){
                     JsonOneData unSuccessJsonType = new JsonOneData("NOT OK");
                     return unSuccessJsonType;
                 }

                 else {

                     JsonOneData unSuccessJsonType = new JsonOneData("OK");
                     billingInformation.setAckStatus("Acknowledged");
                     billingRepository.save(billingInformation);
                     return unSuccessJsonType;
                 }
                }
            }
        }


        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;
    }

    @PostMapping(value = "/secured/callBackApi/")
    public Object callBack(
           @RequestBody JsonSend data) {

        BillingInformation billingInformation =
                billingRepository.findByBankTranxnIDAndTranID(
                        data.getData().getParameters().getBankTranxnID(),
                        data.getData().getParameters().getTranID()
                );

        LocalDateTime paymentTime = billingInformation.getPayDate();
        LocalDateTime localDateTime = LocalDateTime.now();
        long minutesBetween = ChronoUnit.MINUTES.between(localDateTime,paymentTime);

        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())) {
                    {
                        if (minutesBetween >= 15 &&
                                billingInformation.getAckStatus().equals("Processing"))
                            billingInformation.setAckStatus("Call back sent");
                        billingRepository.save(billingInformation);
                        JsonType successJsonType = new JsonType("Successful",
                                "it is done");
                        return successJsonType;
                    }

                }

            }
        }


            JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                    "username or password  is wrong and it is not stakeholder_api user");
            return unSuccessJsonType;
        }



    @PostMapping(value = "/secured/Reconciliationapi/")
    public Object reconciliation(
           @RequestBody JsonSend data) {

        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())){
                    if(data.getData().getParameters().getType().equals("summary")){

                    }
                }
            }
        }


        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;


    }



}



