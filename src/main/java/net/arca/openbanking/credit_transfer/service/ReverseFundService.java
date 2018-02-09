package net.arca.openbanking.credit_transfer.service;


import net.arca.openbanking.credit_transfer.domain.CreditTransferDetails;
import net.arca.openbanking.credit_transfer.domain.MifosDepositResponse;
import net.arca.openbanking.credit_transfer.domain.MifosWithdrawalResponse;
import net.arca.openbanking.credit_transfer.enums.TransactionStatus;
import net.arca.openbanking.credit_transfer.exception.TransactionNotFoundException;
import net.arca.openbanking.credit_transfer.repository.AccountRepo;
import net.arca.openbanking.credit_transfer.repository.CreditTransferRepo;
import net.arca.openbanking.credit_transfer.repository.MifosDepositResponseRepo;
import net.arca.openbanking.credit_transfer.repository.MifosWithdrawalResponseRepo;
import net.arca.openbanking.credit_transfer.request.MifosDepositRequest;
import net.arca.openbanking.credit_transfer.response.MifosCreditTransferResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReverseFundService {

    @Autowired
    CreditTransferRepo creditTransferRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private MifosDepositResponseRepo mifosDepositResponseRepo;

    @Autowired
    private MifosWithdrawalResponseRepo mifosWithdrawalResponseRepo;

    public long reverseFund(long referenceId) throws TransactionNotFoundException {


        final CreditTransferDetails creditTransferByReferenceId = creditTransferRepo
                .findByReferenceId(referenceId, TransactionStatus.SUCCESS);

        if (creditTransferByReferenceId == null) {
            throw new TransactionNotFoundException("Transaction not found.");
        }

        if (creditTransferByReferenceId.getSenderBankCode()
                .equalsIgnoreCase(creditTransferByReferenceId.getBeneficiaryBankCode())) { //reverse only works for 028

            final int creditReversalStatus = undoCreditTrxInMifos(creditTransferByReferenceId);

            logger.debug("Resource id of credit fund reversal ===> " + creditReversalStatus + " =====================|");

            if (String.valueOf(creditReversalStatus) != null) {
                final int debitReversalStatus = undoDebitTrxInMifos(creditTransferByReferenceId);
                logger.debug("Resource id for debit reversal ===> " + debitReversalStatus + " ==================|");
            }

        } else {
            // Todo: non 028 will be handle by switch
        }

        return logCreditTransferDetails(creditTransferByReferenceId); //log a new credit transfer in the db and mark it as reversed.

    }

    private int undoDebitTrxInMifos(CreditTransferDetails creditTransferDetails) {
        //fetch resource id from mifos-withdrawal-response table since its reverse

        MifosWithdrawalResponse mifosWithdrawalResponse = mifosWithdrawalResponseRepo
                .findByReferenceId(creditTransferDetails.getReferenceId());

        return undoMifosTransaction(mifosWithdrawalResponse.getResourceId(), creditTransferDetails.getSenderAccountId());
    }

    private long logCreditTransferDetails(CreditTransferDetails creditTransferByReferenceId) {
        CreditTransferDetails reversedCreditTransferDetails = new CreditTransferDetails();
        reversedCreditTransferDetails.setStatus(TransactionStatus.REVERSED);
        reversedCreditTransferDetails.setOTP(creditTransferByReferenceId.getOTP());
        reversedCreditTransferDetails.setUsername(creditTransferByReferenceId.getUsername());
        reversedCreditTransferDetails.setTransactionDate(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        reversedCreditTransferDetails.setSenderBankCode(creditTransferByReferenceId.getSenderBankCode());
        reversedCreditTransferDetails.setSenderAccountNo(creditTransferByReferenceId.getSenderAccountNo());
        reversedCreditTransferDetails.setSenderAccountName(creditTransferByReferenceId.getSenderAccountName());
        reversedCreditTransferDetails.setSenderAccountId(creditTransferByReferenceId.getSenderAccountId());
        reversedCreditTransferDetails.setRemarks(creditTransferByReferenceId.getRemarks());
        reversedCreditTransferDetails.setBeneficiaryAccountName(creditTransferByReferenceId.getBeneficiaryAccountName());
        reversedCreditTransferDetails.setAmount(creditTransferByReferenceId.getAmount());
        reversedCreditTransferDetails.setBeneficiaryAccountNo(creditTransferByReferenceId.getBeneficiaryAccountNo());
        reversedCreditTransferDetails.setBeneficiaryBankCode(creditTransferByReferenceId.getBeneficiaryBankCode());
        reversedCreditTransferDetails.setReferenceId(creditTransferByReferenceId.getReferenceId());

        final CreditTransferDetails savedReversedCreditTransferDetails
                = creditTransferRepo.save(reversedCreditTransferDetails);

        logger.debug("transaction id of reversed funds ===> " + savedReversedCreditTransferDetails.getTransactionId() + " ===========|");

        return savedReversedCreditTransferDetails.getTransactionId();
    }


    public String getBeneficiaryAccountId(String accountNo) {
        return accountRepo.findByAccountNo(accountNo).getAccountId();
    }

    public int undoCreditTrxInMifos(CreditTransferDetails creditTransferRequest){

        logger.debug("================> Posting debit leg to mifos ================| ");

        //fetch resource id from mifos-deposit-response table since its reverse
        MifosDepositResponse mifosDepositResponse = mifosDepositResponseRepo
                .findByReferenceId(creditTransferRequest.getReferenceId());

        return undoMifosTransaction(mifosDepositResponse.getResourceId(), getBeneficiaryAccountId(creditTransferRequest.getBeneficiaryAccountNo()));

    }

    private int undoMifosTransaction(String resourceId, String accountId) {
        MifosCreditTransferResponse depositResponseEntityBody = null;
        URI uri = null;

        try {
            uri = new URI("https://mifos.arca-infra.com:8443/fineract-provider/api/v1/savingsaccounts/"
                    + accountId + "/transactions/" + resourceId + "?command=undo&tenantIdentifier=default&pretty=true");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        RequestEntity<MifosDepositRequest> request = RequestEntity
                .post(uri)
                .header("X-Mifosconfig-Platform-TenantID", "default")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(null); //undo does not have request body

        final ResponseEntity<MifosCreditTransferResponse> mifosDepositResponseEntity = restTemplate
                .exchange(request, MifosCreditTransferResponse.class);

        depositResponseEntityBody =
                mifosDepositResponseEntity.getBody();

        logger.debug("Is undo of transaction successful ===> " + depositResponseEntityBody.getResourceId() + " ===============|");
        return depositResponseEntityBody.getResourceId();

    }

}
