package com.ssl.billpaymentapi.BillPaymentApi.controller;



import com.ssl.billpaymentapi.BillPaymentApi.model.*;
import com.ssl.billpaymentapi.BillPaymentApi.repository.BillingRepository;
import com.ssl.billpaymentapi.BillPaymentApi.repository.StakeholderRepository;
import com.ssl.billpaymentapi.BillPaymentApi.repository.UsersRepository;
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

    Float totalAmount = 0.0f;
    Float totalSuccessAmount = 0.0f;
    Float totalFailedAmount = 0.0f;

    int totalCount,totalSuccessCount,totalFailedCount;



    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @PostMapping(value = "/secured/singleBillInformation/")
    public Object findOneBillInformation(
    @RequestBody JsonSend data){



        Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())){
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
        Float paidAmount = data.getData().getParameters().getBillAmount();
        String bankTranID = data.getData().getParameters().getTranID();
        String tranxID = billingInformation.getTranID();
        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        StakeHolderInfo stakeHolderInfo = stakeholderRepository.findOne(users.getStakeholderID().getId());

        try {



            Float totalAmount = billingInformation.getTotalAmount();
            Float due_amount = totalAmount - paidAmount;





            billingInformation.setPaidAmount(paidAmount);
            billingInformation.setTotalAmount(totalAmount);
            billingInformation.setDueAmount(due_amount);
            billingInformation.setPayDate(LocalDateTime.now());
            billingInformation.setPaidBy(data.getData().getAccess_info().getUsername());
            billingInformation.setBankTranxnID(data.getData().getParameters().getTranID());
            billingInformation.setStakeHolderInfo(stakeHolderInfo);
            billingInformation.setBillingStatus(BillingStatus.paid);
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
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())){
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

            if (!billInfo.getBillingStatus().getMeaning().equals("paid")) {
                billingInformationUnpaidList.add(billInfo);
            }


        }



        Users  users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
        Set<Roles> rolesSet =  users.getRolesSet();
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())){
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
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())){
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
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())){
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
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())){
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
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())) {
                    {
                        if (minutesBetween >= 15 &&
                                billingInformation.getAckStatus().equals("Processing"))
                            billingInformation.setAckStatus("Call back sent");
                        billingRepository.save(billingInformation);
                        JsonOneData successJsonType = new JsonOneData(
                                "call Back send to client");
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
        List<BillingInformation> billingInformationSuccessList;
        List<BillingInformation> billingInformationFailedList ;
        for(Roles r: rolesSet){
            if(r.getTitle().equals("stakeholder_api")){
                if(passwordEncoder().matches(data.getData().getAccess_info().getPassword(),
                        users.getPassword())&&
                        data.getData().getAccess_info().getUsername().equals(users.getUserName())){
                    if(data.getData().getParameters().getType().equals("summary")){
                      List<BillingInformation>   billingInformationList =
                                billingRepository.findAllByIssueDate(data.getData().getParameters().getDateTime());
                     totalCount = billingInformationList.size();


                      for(BillingInformation billingInformation:billingInformationList){
                           totalAmount += billingInformation.getTotalAmount();
                      }

                    billingInformationSuccessList = billingRepository.findAllByIssueDateAndBillStatus(
                            data.getData().getParameters().getDateTime(),BillingStatus.paid);

                  totalSuccessCount =     billingInformationSuccessList.size();

                   for (BillingInformation billingInformation:billingInformationSuccessList)  {
                       totalSuccessAmount += billingInformation.getPaidAmount();
                   }

                        billingInformationFailedList = billingRepository.findAllByIssueDateAndBillStatus(
                                data.getData().getParameters().getDateTime(),BillingStatus.Failed);

                        totalFailedCount =     billingInformationFailedList.size();

                        for (BillingInformation billingInformation:billingInformationFailedList)  {
                            totalFailedAmount += billingInformation.getTotalAmount();
                        }



                        JsonSummary summary = new JsonSummary(totalCount,totalAmount,
                                totalSuccessCount,totalSuccessAmount,totalFailedCount,
                                totalFailedAmount);
                      return  summary;
                    }
                    if(data.getData().getParameters().getType().equals("details")){
                        List<BillingInformation>   billingInformationList =
                                billingRepository.findAllByIssueDate(data.getData().getParameters().getDateTime());
                        totalCount = billingInformationList.size();


                        for(BillingInformation billingInformation:billingInformationList){
                            totalAmount += billingInformation.getTotalAmount();
                        }

                        billingInformationSuccessList = billingRepository.findAllByIssueDateAndBillStatus(
                                data.getData().getParameters().getDateTime(),BillingStatus.paid);

                        totalSuccessCount =     billingInformationSuccessList.size();

                        for (BillingInformation billingInformation:billingInformationSuccessList)  {
                            totalSuccessAmount += billingInformation.getPaidAmount();
                        }

                        billingInformationFailedList = billingRepository.findAllByIssueDateAndBillStatus(
                                data.getData().getParameters().getDateTime(),BillingStatus.Failed);

                        totalFailedCount =     billingInformationFailedList.size();

                        for (BillingInformation billingInformation:billingInformationFailedList)  {
                            totalFailedAmount += billingInformation.getTotalAmount();
                        }



                        JsonDetails details = new JsonDetails(totalCount,totalAmount,
                                totalSuccessCount,totalSuccessAmount,totalFailedCount,
                                totalFailedAmount,billingInformationList);
                        return  details;
                    }
                }
            }
        }


        JsonType unSuccessJsonType = new JsonType("Unsuccessful",
                "username or password  is wrong and it is not stakeholder_api user");
        return unSuccessJsonType;


    }



}



