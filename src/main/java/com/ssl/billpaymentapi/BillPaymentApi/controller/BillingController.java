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

        JsonType response = new JsonType();
        try {
            if(data!=null){
                if(data.getData()!=null){
                    if(data.getData().getAccess_info()!=null) {
                        if(data.getData().getAccess_info().getUsername()==null){
                            return response = new JsonType("Unsuccessful", "username is required");
                        }
                        if(data.getData().getAccess_info().getPassword()==null){
                            return response = new JsonType("Unsuccessful", "password is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Access info is required");
                    }
                    if(data.getData().getParameters()!=null) {
                        if(data.getData().getParameters().getBillNumber()==null) {
                            return response = new JsonType("Unsuccessful", "billNumber is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Parameters is required");
                    }
                }else{
                    return response = new JsonType("Unsuccessful", "JSON data is not in correct format");
                }
            }else{
                return response = new JsonType("Unsuccessful", "Request data is not in correct format");
            }

            Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {
                                BillingInformation bill =  billingRepository.findByBillNumber(data.getData().getParameters().getBillNumber());
                                if (bill == null) {
                                    response = new JsonType("Unsuccessful", "No Data found Against this bill number");
                                }else{
                                    SingleBillInformation singleBillInformation =
                                            new SingleBillInformation("Successful",bill.getId(),bill.getBillNumber(),bill.getBillAmount(),
                                                    bill.getBillStatus().getMeaning(),bill.getCancelDate(),bill.getCancelled_by(),
                                                    bill.getCustomerNumber(),bill.getDueAmount(),bill.getPaidAmount(),
                                                    bill.getTotalAmount(),bill.getPaidBy(),bill.getVatAmount(),bill.getPayDate(),
                                                    bill.getAckStatus(),bill.getBankTranxnID(),bill.getTranID());
                                    return singleBillInformation;
                                }
                            }else{
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        }catch (Exception ex){
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return response;
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



        JsonType response = new JsonType();
        try {
            if(data!=null){
                if(data.getData()!=null){
                    if(data.getData().getAccess_info()!=null) {
                        if(data.getData().getAccess_info().getUsername()==null){
                            return response = new JsonType("Unsuccessful", "username is required");
                        }
                        if(data.getData().getAccess_info().getPassword()==null){
                            return response = new JsonType("Unsuccessful", "password is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Access info is required");
                    }
                    if(data.getData().getParameters()!=null) {
                        if(data.getData().getParameters().getBillNumber()==null) {
                            return response = new JsonType("Unsuccessful", "billNumber is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Parameters is required");
                    }
                }else{
                    return response = new JsonType("Unsuccessful", "JSON data is not in correct format");
                }
            }else{
                return response = new JsonType("Unsuccessful", "Request data is not in correct format");
            }


            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {

                                if (jsonType == null) {
                                    response = new JsonType("Unsuccessful", "No Data found Against this bill number");
                                }else{
                                    return jsonType;
                                }
                            }else{
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        }catch (Exception ex){
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return response;
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



        JsonType response = new JsonType();
        try {
            if(data!=null){
                if(data.getData()!=null){
                    if(data.getData().getAccess_info()!=null) {
                        if(data.getData().getAccess_info().getUsername()==null){
                            return Collections.singletonList(response = new JsonType("Unsuccessful", "username is required"));
                        }
                        if(data.getData().getAccess_info().getPassword()==null){
                            return Collections.singletonList(response = new JsonType("Unsuccessful", "password is required"));
                        }
                    }else{
                        return Collections.singletonList(response = new JsonType("Unsuccessful", "Access info is required"));
                    }
                    if(data.getData().getParameters()!=null) {
                        if(data.getData().getParameters().getBillNumber()==null) {
                            return Collections.singletonList(response = new JsonType("Unsuccessful", "billNumber is required"));
                        }
                    }else{
                        return Collections.singletonList(response = new JsonType("Unsuccessful", "Parameters is required"));
                    }
                }else{
                    return Collections.singletonList(response = new JsonType("Unsuccessful", "JSON data is not in correct format"));
                }
            }else{
                return Collections.singletonList(response = new JsonType("Unsuccessful", "Request data is not in correct format"));
            }

            Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {

                                if (billingInformationUnpaidList == null) {
                                    response = new JsonType("Unsuccessful", "No Data found Against this Customer number");
                                }else{
                                    return Collections.singletonList(billingInformationUnpaidList);
                                }
                            }else{
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        }catch (Exception ex){
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return Collections.singletonList(response);
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




        JsonType response = new JsonType();
        try {
            if(data!=null){
                if(data.getData()!=null){
                    if(data.getData().getAccess_info()!=null) {
                        if(data.getData().getAccess_info().getUsername()==null){
                            return response = new JsonType("Unsuccessful", "username is required");
                        }
                        if(data.getData().getAccess_info().getPassword()==null){
                            return response = new JsonType("Unsuccessful", "password is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Access info is required");
                    }
                    if(data.getData().getParameters()!=null) {
                        if(data.getData().getParameters().getBillNumber()==null) {
                            return response = new JsonType("Unsuccessful", "billNumber is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Parameters is required");
                    }
                }else{
                    return response = new JsonType("Unsuccessful", "JSON data is not in correct format");
                }
            }else{
                return response = new JsonType("Unsuccessful", "Request data is not in correct format");
            }

            Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {

                                if (jsonType == null) {
                                    response = new JsonType("Unsuccessful", "No Data found Against this bill number");
                                }else{
                                    return jsonType;
                                }
                            }else{
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        }catch (Exception ex){
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return response;
    }

    @PostMapping(value = "/secured/currentDateBillInformation/")
    public List<Object> findAllCurrentDateBillInformation(
            @RequestBody JsonSend data){


        JsonType response = new JsonType();
        try {
            if(data!=null){
                if(data.getData()!=null){
                    if(data.getData().getAccess_info()!=null) {
                        if(data.getData().getAccess_info().getUsername()==null){
                            return Collections.singletonList(response = new JsonType("Unsuccessful", "username is required"));
                        }
                        if(data.getData().getAccess_info().getPassword()==null){
                            return Collections.singletonList(response = new JsonType("Unsuccessful", "password is required"));
                        }
                    }else{
                        return Collections.singletonList(response = new JsonType("Unsuccessful", "Access info is required"));
                    }
                    if(data.getData().getParameters()!=null) {
                        if(data.getData().getParameters().getBillNumber()==null) {
                            return Collections.singletonList(response = new JsonType("Unsuccessful", "billNumber is required"));
                        }
                    }else{
                        return Collections.singletonList(response = new JsonType("Unsuccessful", "Parameters is required"));
                    }
                }else{
                    return Collections.singletonList(response = new JsonType("Unsuccessful", "JSON data is not in correct format"));
                }
            }else{
                return Collections.singletonList(response = new JsonType("Unsuccessful", "Request data is not in correct format"));
            }

            Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {
                                List<BillingInformation> bill =
                                billingRepository.findAllByIssueDate(Date.valueOf(LocalDate.now()));
                                if (bill == null) {
                                    response = new JsonType("Unsuccessful", "No Data found Against this bill number");
                                }else{
                                    return Collections.singletonList(bill);
                                }
                            }else{
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        }catch (Exception ex){
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return Collections.singletonList(response);
    }

    @PostMapping(value = "/secured/AcknowledgeApi/")
    public Object findAcKnowledgeStatus(
            @RequestBody JsonSend data ){

        JsonType response = new JsonType();
        try {
            if(data!=null){
                if(data.getData()!=null){
                    if(data.getData().getAccess_info()!=null) {
                        if(data.getData().getAccess_info().getUsername()==null){
                            return response = new JsonType("Unsuccessful", "username is required");
                        }
                        if(data.getData().getAccess_info().getPassword()==null){
                            return response = new JsonType("Unsuccessful", "password is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Access info is required");
                    }
                    if(data.getData().getParameters()!=null) {
                        if(data.getData().getParameters().getBillNumber()==null) {
                            return response = new JsonType("Unsuccessful", "billNumber is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Parameters is required");
                    }
                }else{
                    return response = new JsonType("Unsuccessful", "JSON data is not in correct format");
                }
            }else{
                return response = new JsonType("Unsuccessful", "Request data is not in correct format");
            }

            Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {
                                BillingInformation billingInformation =
                                        billingRepository.findByBankTranxnIDAndTranID(
                                                data.getData().getParameters().getBankTranxnID(),
                                                data.getData().getParameters().getTranID()
                                        );
                                if (billingInformation == null) {
                                    response = new JsonType("Unsuccessful", "Not OK");
                                }else{
                                     response = new JsonType("Successful","OK");
                                    billingInformation.setAckStatus("Acknowledged");
                                    billingRepository.save(billingInformation);
                                    return response;
                                }
                            }else{
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        }catch (Exception ex){
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return response;


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

        JsonType response = new JsonType();
        try {
            if(data!=null){
                if(data.getData()!=null){
                    if(data.getData().getAccess_info()!=null) {
                        if(data.getData().getAccess_info().getUsername()==null){
                            return response = new JsonType("Unsuccessful", "username is required");
                        }
                        if(data.getData().getAccess_info().getPassword()==null){
                            return response = new JsonType("Unsuccessful", "password is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Access info is required");
                    }
                    if(data.getData().getParameters()!=null) {
                        if(data.getData().getParameters().getBillNumber()==null) {
                            return response = new JsonType("Unsuccessful", "billNumber is required");
                        }
                    }else{
                        return response = new JsonType("Unsuccessful", "Parameters is required");
                    }
                }else{
                    return response = new JsonType("Unsuccessful", "JSON data is not in correct format");
                }
            }else{
                return response = new JsonType("Unsuccessful", "Request data is not in correct format");
            }

            Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {

                                if (billingInformation == null) {
                                    response = new JsonType("Unsuccessful", "No Data found Against this bill number");
                                }else{
                                    if (minutesBetween >= 15 &&
                                            billingInformation.getAckStatus().equals("Processing")){
                                        billingInformation.setAckStatus("Call back sent");
                                        billingRepository.save(billingInformation);
                                        JsonOneData successJsonType = new JsonOneData(
                                                "call Back send to client");
                                        return successJsonType;
                                    }

                                }
                            }else{
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        }catch (Exception ex){
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return response;
        }



    @PostMapping(value = "/secured/Reconciliationapi/")
    public Object reconciliation(
           @RequestBody JsonSend data) {

        List<BillingInformation>   billingInformationList =
                billingRepository.findAllByIssueDate(data.getData().getParameters().getDateTime());
        List <BillingInformation> billingInformationSuccessList;
        List<BillingInformation> billingInformationFailedList;
        JsonType response = new JsonType();
        try {
            if (data != null) {
                if (data.getData() != null) {
                    if (data.getData().getAccess_info() != null) {
                        if (data.getData().getAccess_info().getUsername() == null) {
                            return response = new JsonType("Unsuccessful", "username is required");
                        }
                        if (data.getData().getAccess_info().getPassword() == null) {
                            return response = new JsonType("Unsuccessful", "password is required");
                        }
                    } else {
                        return response = new JsonType("Unsuccessful", "Access info is required");
                    }
                    if (data.getData().getParameters() != null) {
                        if (data.getData().getParameters().getBillNumber() == null) {
                            return response = new JsonType("Unsuccessful", "billNumber is required");
                        }
                    } else {
                        return response = new JsonType("Unsuccessful", "Parameters is required");
                    }
                } else {
                    return response = new JsonType("Unsuccessful", "JSON data is not in correct format");
                }
            } else {
                return response = new JsonType("Unsuccessful", "Request data is not in correct format");
            }

            Users users = usersRepository.findByUserName(data.getData().getAccess_info().getUsername());
            if (users != null) {
                Set<Roles> rolesSet = users.getRolesSet();
                if (rolesSet != null) {
                    for (Roles r : rolesSet) {
                        if (r.getTitle().equals("stakeholder_api")) {
                            if (passwordEncoder().matches(data.getData().getAccess_info().getPassword(), users.getPassword())
                                    && data.getData().getAccess_info().getUsername().equals(users.getUserName())) {

                                if (billingInformationList == null) {
                                    response = new JsonType("Unsuccessful", "No Data found Against this bill number");
                                } else {
                                    if(data.getData().getParameters().getType().equals("summary")){

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
                            } else {
                                response = new JsonType("Unsuccessful", "User credentials are not correct");
                            }
                        } else {
                            response = new JsonType("Unsuccessful", "Not a valid API user");
                        }
                    }
                }
            } else {
                response = new JsonType("Unsuccessful", "User Not Found");
            }
        } catch (Exception ex) {
            response = new JsonType("Unsuccessful", "Exception Occured");
        }
        return response;
    }



}



