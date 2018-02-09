package net.arca.openbanking.credit_transfer.service;

//import CreditTransferDetails;

import net.arca.openbanking.credit_transfer.request.ConfirmCreditTransferRequest;
import net.arca.openbanking.credit_transfer.request.MifosDepositRequest;
import net.arca.openbanking.credit_transfer.domain.CreditTransferDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CustomObjectMapper {


    public MifosDepositRequest buildMifosDepositRequest(ConfirmCreditTransferRequest confirmCreditTransferRequest) {
        MifosDepositRequest mifosDepositRequest = new MifosDepositRequest();

        mifosDepositRequest.setAccountNumber(confirmCreditTransferRequest.getBeneficiaryAccountNo());
        mifosDepositRequest.setBankNumber("1");
        mifosDepositRequest.setLocale("en");
        mifosDepositRequest.setPaymentTypeId("1");
        mifosDepositRequest.setTransactionAmount(String.valueOf(confirmCreditTransferRequest.getAmount()));
        mifosDepositRequest.setDateFormat("yyyy-MM-dd HH:mm:ss");
        mifosDepositRequest.setTransactionDate(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")).toString());


        System.out.println("Transaction date == > " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        return mifosDepositRequest;
    }

    public MifosDepositRequest buildMifosWithdrawalRequest(ConfirmCreditTransferRequest confirmCreditTransferRequest) {
        MifosDepositRequest mifosDepositRequest = new MifosDepositRequest();

        mifosDepositRequest.setAccountNumber(confirmCreditTransferRequest.getSenderAccountNo());
        mifosDepositRequest.setBankNumber("1");
        mifosDepositRequest.setLocale("en");
        mifosDepositRequest.setPaymentTypeId("1");
        mifosDepositRequest.setTransactionAmount(String.valueOf(confirmCreditTransferRequest.getAmount()));
        mifosDepositRequest.setDateFormat("yyyy-MM-dd HH:mm:ss");
        mifosDepositRequest.setTransactionDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());


        System.out.println("Transaction date == > " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        return mifosDepositRequest;
    }

    public CreditTransferDetails buildCreditTransferDetails(net.arca.openbanking.credit_transfer.request.CreditTransferDetails creditTransferRequest) {
        CreditTransferDetails creditTransferDetails = new CreditTransferDetails();

        creditTransferDetails.setAmount(creditTransferRequest.getAmount());
        creditTransferDetails.setBeneficiaryAccountName(creditTransferRequest.getBeneficiaryAccountName());
        creditTransferDetails.setBeneficiaryAccountNo(creditTransferRequest.getBeneficiaryAccountNo());
        creditTransferDetails.setBeneficiaryBankCode(creditTransferRequest.getBeneficiaryBankCode());
        creditTransferDetails.setOTP(generateOTP());
        creditTransferDetails.setRemarks(creditTransferRequest.getRemarks());
        creditTransferDetails.setSenderAccountId(creditTransferRequest.getSenderAccountId());
        creditTransferDetails.setSenderAccountName(creditTransferRequest.getSenderAccountName());
        creditTransferDetails.setSenderAccountNo(creditTransferRequest.getSenderAccountNo());
        creditTransferDetails.setSenderBankCode(creditTransferRequest.getSenderBankCode());
        creditTransferDetails.setTransactionDate(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        creditTransferDetails.setUsername(creditTransferRequest.getUsername());

        return creditTransferDetails;
    }

    private int generateOTP() {
        return 1234;
    } //Todo: call external api to generate OTP


    public long generate7digitNumber() {
        //use Math.random method to generate 10 digit number
        return (long) Math.floor(Math.random() * 9_000_000L) + 1_000_000L;
    }
}
