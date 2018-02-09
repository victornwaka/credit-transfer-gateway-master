package net.arca.openbanking.credit_transfer.service;


import net.arca.openbanking.credit_transfer.enums.TransactionStatus;
import net.arca.openbanking.credit_transfer.exception.CreditTransferValidationException;
import net.arca.openbanking.credit_transfer.repository.CreditTransferRepo;
import net.arca.openbanking.credit_transfer.request.ConfirmCreditTransferRequest;
import net.arca.openbanking.credit_transfer.request.CreditTransferDetails;
import net.arca.openbanking.credit_transfer.validation.CreditTransferRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class CreditTransferServiceSupport {
    @Autowired
    private CreditTransferRepo creditTransferRepo;


    public boolean sendOTPToCustomerPhoneNumber(int otp) {
        return true;
    }

    public Optional<String> validateCreditTransferRequest(CreditTransferDetails creditTransferRequest) {

        return (CreditTransferRequestValidation.isSenderAccountNo()
                .and(CreditTransferRequestValidation.isBeneficiaryAccountNo())
                .and(CreditTransferRequestValidation.senderBankCode())
                .and(CreditTransferRequestValidation.beneficiaryBankCode())
                .and(CreditTransferRequestValidation.isSenderAccountNameEmpty())
                .and(CreditTransferRequestValidation.isBeneficiaryAccountNameEmpty())
                .and(CreditTransferRequestValidation.isCurrencyValid())
                .and(CreditTransferRequestValidation.isSenderAccountNameEmpty()).apply(creditTransferRequest)
                .getReason());
    }

    public boolean validateConfirmCreditRequest(ConfirmCreditTransferRequest confirmCreditTransferRequest)
            throws CreditTransferValidationException {

        net.arca.openbanking.credit_transfer.domain.CreditTransferDetails creditTransferDetails = creditTransferRepo.findByStatus(
                confirmCreditTransferRequest.getTransactionId(), TransactionStatus.NOT_PROCESSED);

        if (creditTransferDetails != null) {
            System.out.println("creditTransferDetails content ===> " + creditTransferDetails.toString() + " ==============|");
        }else{
            System.out.println("***********  creditTransferDetails is null *********");
        }


        if (creditTransferDetails == null) {
            throw new CreditTransferValidationException("Credit transfer confirmation validation failed; transactionId not found.");
        }

        if (confirmCreditTransferRequest.getAmount()
                != creditTransferDetails.getAmount()) {
            System.out.println("=======> amount failed <========");
            return false;
        } else if (!confirmCreditTransferRequest.getBeneficiaryAccountName()
                .equalsIgnoreCase(creditTransferDetails.getBeneficiaryAccountName())) {
            System.out.println("=======> BeneficiaryAccountName failed <========");
            return false;
        } else if (!confirmCreditTransferRequest.getBeneficiaryAccountNo()
                .equalsIgnoreCase(creditTransferDetails.getBeneficiaryAccountNo())) {
            System.out.println("=======> BeneficiaryAccountNo failed <========");
            return false;
        } else if (!confirmCreditTransferRequest.getBeneficiaryBankCode()
                .equalsIgnoreCase(creditTransferDetails.getBeneficiaryBankCode())) {
            System.out.println("=======> BeneficiaryBankCode failed <========");
            return false;
        } else if (confirmCreditTransferRequest.getOTP() != creditTransferDetails.getOTP()) {
            System.out.println("=======> OTP failed <========");
            return false;
        } else if (!confirmCreditTransferRequest.getSenderAccountId()
                .equalsIgnoreCase(creditTransferDetails.getSenderAccountId())) {
            System.out.println("=======> SenderAccountId failed <========");
            return false;
        } else if (!confirmCreditTransferRequest.getSenderAccountName()
                .equalsIgnoreCase(creditTransferDetails.getSenderAccountName())) {
            System.out.println("=======> SenderAccountName failed <========");
            return false;
        } else if (!confirmCreditTransferRequest.getSenderAccountNo()
                .equalsIgnoreCase(creditTransferDetails.getSenderAccountNo())) {
            System.out.println("=======> SenderAccountNo failed <========");
            return false;
        } else if (!confirmCreditTransferRequest.getSenderBankCode()
                .equalsIgnoreCase(creditTransferDetails.getSenderBankCode())) {
            System.out.println("=======> SenderBankCode failed <========");
            return false;
        }

        return true;
    }

    public boolean validateOTP(int otp) {
        //Todo: validate otp to confirm it has not expired.
        if (otp == 1234) {
            return true;
        } else {
            return false;
        }
    }

}
