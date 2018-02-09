package net.arca.openbanking.credit_transfer.validation;


import net.arca.openbanking.credit_transfer.config.constants.Constants;
import net.arca.openbanking.credit_transfer.request.CreditTransferDetails;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface CreditTransferRequestValidation extends Function<CreditTransferDetails, ValidationResult> {

//    static CreditTransferRequestValidation isRequestIdEmpty() {
//        return holds(creditTransferRequestPredicate -> !creditTransferRequestPredicate.getRequestId().trim().isEmpty(),
//                Constants.RequestId_is_empty);
//    }

    static CreditTransferRequestValidation isCurrencyValid() {
        return holds(creditTransferRequestPredicate -> creditTransferRequestPredicate.getCurrency().trim().equals("NGN"),
                Constants.Invalid_currency);
    }

    static CreditTransferRequestValidation isSenderAccountNameEmpty() {
        return holds(creditTransferRequestPredicate -> !creditTransferRequestPredicate.getSenderAccountName().trim().isEmpty(),
                Constants.Account_holder_is_empty);
    }

    static CreditTransferRequestValidation isBeneficiaryAccountNameEmpty() {
        return holds(creditTransferRequestPredicate -> !creditTransferRequestPredicate.getBeneficiaryAccountName().trim().isEmpty(),
                Constants.Account_holder_is_empty);
    }

    static CreditTransferRequestValidation beneficiaryBankCode() {
        return holds(creditTransferRequestPredicate -> Pattern.compile("^[0-9]{3}$")
                        .matcher(creditTransferRequestPredicate.getBeneficiaryBankCode()
                                .trim()).matches(),
                Constants.Bank_code_is_invalid);
    }

    static CreditTransferRequestValidation senderBankCode() {
        return holds(creditTransferRequestPredicate -> Pattern.compile("^[0-9]{3}$")
                        .matcher(creditTransferRequestPredicate.getSenderBankCode()
                                .trim()).matches(),
                Constants.Bank_code_is_invalid);
    }

    static CreditTransferRequestValidation isSenderAccountNo() {
        return holds(creditTransferRequestPredicate -> Pattern.compile("^[0-9]{10}$")
                        .matcher(creditTransferRequestPredicate.getSenderAccountNo()
                                .trim()).matches(),
                Constants.Invalid_account_no);
    }

    static CreditTransferRequestValidation isBeneficiaryAccountNo() {
        return holds(creditTransferRequestPredicate -> Pattern.compile("^[0-9]{10}$")
                        .matcher(creditTransferRequestPredicate.getBeneficiaryAccountNo()
                                .trim()).matches(),
                Constants.Invalid_account_no);
    }


    static CreditTransferRequestValidation holds(Predicate<CreditTransferDetails> p, String message) {
        return creditTransferRequest -> p.test(creditTransferRequest) ? ValidationResult.valid() : ValidationResult.invalid(message);
    }


    default CreditTransferRequestValidation and(CreditTransferRequestValidation other) {
        return disputeRequest -> {
            final ValidationResult result = this.apply(disputeRequest);
            return result.isValid() ? other.apply(disputeRequest) : result;
        };
    }
}
