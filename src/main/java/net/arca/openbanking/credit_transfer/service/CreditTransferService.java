package net.arca.openbanking.credit_transfer.service;

import net.arca.openbanking.credit_transfer.config.Mifosconfig;
import net.arca.openbanking.credit_transfer.domain.CreditTransferDetails;
import net.arca.openbanking.credit_transfer.domain.MifosDepositResponse;
import net.arca.openbanking.credit_transfer.domain.MifosWithdrawalResponse;
import net.arca.openbanking.credit_transfer.enums.TransactionStatus;
import net.arca.openbanking.credit_transfer.exception.AccountNotFoundException;
import net.arca.openbanking.credit_transfer.exception.CreditTransferValidationException;
import net.arca.openbanking.credit_transfer.exception.OTPMismatchException;
import net.arca.openbanking.credit_transfer.exception.UserNotFoundException;
import net.arca.openbanking.credit_transfer.repository.*;
import net.arca.openbanking.credit_transfer.request.ConfirmCreditTransferRequest;
import net.arca.openbanking.credit_transfer.request.MifosDepositRequest;
import net.arca.openbanking.credit_transfer.response.CreditTransferResponse;
import net.arca.openbanking.credit_transfer.response.MifosCreditTransferResponse;
import net.arca.openbanking.credit_transfer.validation.AccountValidation;
import net.arca.openbanking.credit_transfer.validation.CustomerValidation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CreditTransferService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CreditTransferRepo creditTransferRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MifosDepositResponseRepo mifosDepositResponseRepo;

    @Autowired
    private MifosWithdrawalResponseRepo mifosWithdrawalResponseRepo;

    @Autowired
    private Mifosconfig mifosConfig;

    @Autowired
    private CreditTransferServiceSupport creditTransferServiceSupport;

    CustomObjectMapper customObjectMapper = new CustomObjectMapper();

    public Long logCreditTransferRequest(net.arca.openbanking.credit_transfer.request.CreditTransferDetails creditTransferRequest)
            throws CreditTransferValidationException, AccountNotFoundException, UserNotFoundException {

        AccountValidation accountValidation = new AccountValidation(accountRepo);
        CustomerValidation customerValidation = new CustomerValidation(userRepo);

        if (creditTransferServiceSupport.validateCreditTransferRequest(creditTransferRequest).isPresent()) { //are there validation errors
            throw new CreditTransferValidationException(creditTransferServiceSupport.validateCreditTransferRequest(creditTransferRequest).get());

        } else if (!accountValidation.doesAccountIdExist(creditTransferRequest.getSenderAccountId())
                || !accountValidation.doesAccountNoExist(creditTransferRequest.getSenderAccountNo())) {
            throw new AccountNotFoundException("Sender account not found.");

        } else if (!customerValidation.doesUserExist(creditTransferRequest.getUsername())) {
            throw new UserNotFoundException("User not found.");
        }

        if (creditTransferRequest.getSenderBankCode().equalsIgnoreCase(creditTransferRequest.getBeneficiaryBankCode())) {
            if (!accountValidation.doesAccountNoExist(creditTransferRequest.getBeneficiaryAccountNo())) {
                throw new AccountNotFoundException("Beneficiary account not found.");
            }
        } else {
            throw new CreditTransferValidationException("Recipient bank not onboard yet."); //if bank codes not equal call mifos middleware to forward credit transfer switch
        }

        final CreditTransferDetails creditTransferDetails = customObjectMapper.buildCreditTransferDetails(creditTransferRequest);


        final int OTP = creditTransferDetails.getOTP();
        creditTransferDetails.setOTP(OTP);
        creditTransferDetails.setStatus(TransactionStatus.NOT_PROCESSED);

        final CreditTransferDetails savedCreditTransferDetails = creditTransferRepo.save(creditTransferDetails);

        boolean result = creditTransferServiceSupport.sendOTPToCustomerPhoneNumber(OTP);

        logger.debug("result of sending OTP to customer ==> " + result + "===================|");

        return savedCreditTransferDetails.getTransactionId();

    }


    private String findMifosURLForCreditTransfer(String parameter, String bankCode) throws CreditTransferValidationException {
        String MIFOS_INSTANCE_1_URL;
        String MIFOS_INSTANCE_2_URL;
        if (bankCode.equals(mifosConfig.getInstancecode1())) {

            MIFOS_INSTANCE_1_URL = "https://" + mifosConfig.getInstance1ip() + ":" + mifosConfig.getPort() + "/fineract-provider/api/v1/savingsaccounts/"
                    + parameter + "/transactions?command=deposit&tenantIdentifier=default&pretty=true";
            logger.debug("MIFOS_INSTANCE_1_URL ==> " + MIFOS_INSTANCE_1_URL + " <== MIFOS_INSTANCE_1_URL");
            return MIFOS_INSTANCE_1_URL;
        } else if (bankCode.equals(mifosConfig.getInstancecode2())) {

            MIFOS_INSTANCE_2_URL = "https://" + mifosConfig.getInstance2ip() + ":" + mifosConfig.getPort() + "/fineract-provider/api/v1/savingsaccounts/"
                    + parameter + "/transactions?command=deposit&tenantIdentifier=default&pretty=true";
            logger.debug("MIFOS_INSTANCE_2_URL ==> " + MIFOS_INSTANCE_2_URL + " <== MIFOS_INSTANCE_2_URL");
            return MIFOS_INSTANCE_2_URL;
        } else throw new CreditTransferValidationException("Invalid Instructed institution code");

    }

    private String findMifosURLForDebitTransfer(String parameter, String bankCode) throws CreditTransferValidationException {
        String MIFOS_INSTANCE_1_URL;
        String MIFOS_INSTANCE_2_URL;
        if (bankCode.equals(mifosConfig.getInstancecode1())) {

            MIFOS_INSTANCE_1_URL = "https://" + mifosConfig.getInstance1ip() + ":" + mifosConfig.getPort() + "/fineract-provider/api/v1/savingsaccounts/"
                    + parameter + "/transactions?command=withdrawal&tenantIdentifier=default&pretty=true";
            logger.debug("MIFOS_INSTANCE_1_URL ==> " + MIFOS_INSTANCE_1_URL + " <== MIFOS_INSTANCE_1_URL");
            return MIFOS_INSTANCE_1_URL;

        } else if (bankCode.equals(mifosConfig.getInstancecode2())) {

            MIFOS_INSTANCE_2_URL = "https://" + mifosConfig.getInstance2ip() + ":" + mifosConfig.getPort() + "/fineract-provider/api/v1/savingsaccounts/"
                    + parameter + "/transactions?command=withdrawal&tenantIdentifier=default&pretty=true";
            logger.debug("MIFOS_INSTANCE_2_URL ==> " + MIFOS_INSTANCE_2_URL + " <== MIFOS_INSTANCE_2_URL");
            return MIFOS_INSTANCE_2_URL;
        } else throw new CreditTransferValidationException("Invalid Instructed institution code");

    }


    @Transactional
    public CreditTransferResponse processCreditTransfer(ConfirmCreditTransferRequest confirmCreditTransferRequest)
            throws CreditTransferValidationException, URISyntaxException, OTPMismatchException {

        MifosCreditTransferResponse mifosDepositResponse = null;
        MifosCreditTransferResponse mifosWithdrawalResponse = null;
        Long referenceId = null;

        boolean isOTPValid = creditTransferServiceSupport.validateOTP(confirmCreditTransferRequest.getOTP());

        logger.debug("is otp valid ====> " + isOTPValid + " ===============|");
        boolean isRequestValid = creditTransferServiceSupport.validateConfirmCreditRequest
                (confirmCreditTransferRequest);
        logger.debug("is request valid ===== > " + isRequestValid + " ===============|");

        if (!isRequestValid) {
            throw new CreditTransferValidationException("Credit transfer details mismatch.");
        }

        if (isOTPValid) {

            if (confirmCreditTransferRequest.getSenderBankCode().equalsIgnoreCase(confirmCreditTransferRequest.getBeneficiaryBankCode())) {
                // this transaction is for 028 to 028
                mifosWithdrawalResponse = postDebitTransferToMifos(confirmCreditTransferRequest);

                logger.debug("Is debit leg successful ===> " + mifosWithdrawalResponse.getResourceId() + " ======================|");
                if (mifosWithdrawalResponse.getResourceId() != null) { //debit leg was successful, post credit leg
                    mifosDepositResponse = postCreditTransferToMifos(confirmCreditTransferRequest,
                            mifosWithdrawalResponse);
                }


            } else {
                //Todo: for non 028 to non 028 forward the credit transfer details to the senders bank channel to forward to switch and return response back to open banking api
                //Todo: for 028 to non 028 forward request to mifos middleware to forward to switch and return response back to open banking api
            }

            logger.debug("mifosCreditTransferResponse ====> " + mifosDepositResponse.toString()
                    + " =============|");
            logger.debug("mifosCreditTransferResponse1 =====> " + mifosWithdrawalResponse.toString()
                    + " ==============|");


            referenceId = customObjectMapper.generate7digitNumber();

            logger.debug("reference id  ==== > " + referenceId + " =============|");

            logMifosDepositResponse(mifosDepositResponse, referenceId);
            logMifosWithdrawalResponse(mifosWithdrawalResponse, referenceId);

            creditTransferRepo.updateCreditTransferDetails(TransactionStatus.SUCCESS,
                    referenceId, Long.valueOf(confirmCreditTransferRequest.getTransactionId()));


            return new CreditTransferResponse(Long.valueOf(confirmCreditTransferRequest.getTransactionId()),
                    referenceId, "", "");

        } else {
            throw new OTPMismatchException("OTP may have expired or invalid.");
        }

    }


    private void logMifosDepositResponse(MifosCreditTransferResponse mifosCreditTransferResponse, long referenceId) {

        MifosDepositResponse mdr = new MifosDepositResponse();
        mdr.setAccountNumber((mifosCreditTransferResponse.getChanges().getAccountNumber() != null) ? mifosCreditTransferResponse.getChanges().getAccountNumber() : "");
        mdr.setBankNumber((mifosCreditTransferResponse.getChanges().getBankNumber() != null) ? mifosCreditTransferResponse.getChanges().getBankNumber() : "");
        mdr.setCheckNumber((mifosCreditTransferResponse.getChanges().getCheckNumber() != null) ? mifosCreditTransferResponse.getChanges().getCheckNumber() : "");
        mdr.setClientId((String.valueOf(mifosCreditTransferResponse.getClientId()) != null) ? String.valueOf(mifosCreditTransferResponse.getClientId()) : "");
        mdr.setOfficeId((String.valueOf(mifosCreditTransferResponse.getOfficeId()) != null) ? String.valueOf(mifosCreditTransferResponse.getOfficeId()) : "");
        mdr.setReferenceId(referenceId);

        mdr.setResourceId(String.valueOf(mifosCreditTransferResponse.getResourceId()));
        mdr.setRoutingCode(mifosCreditTransferResponse.getChanges().getRoutingCode());
        mdr.setSavingsId(mifosCreditTransferResponse.getSavingsId().toString());

        mdr.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .toString());

        mifosDepositResponseRepo.save(mdr);
    }

    private void logMifosWithdrawalResponse(MifosCreditTransferResponse mifosCreditTransferResponse, long referenceId) {

        MifosWithdrawalResponse mwr = new MifosWithdrawalResponse();

        mwr.setAccountNumber((mifosCreditTransferResponse.getChanges().getAccountNumber() != null) ? mifosCreditTransferResponse.getChanges().getAccountNumber() : "");
        mwr.setBankNumber((mifosCreditTransferResponse.getChanges().getBankNumber() != null) ? mifosCreditTransferResponse.getChanges().getBankNumber() : "");
        mwr.setCheckNumber((mifosCreditTransferResponse.getChanges().getCheckNumber() != null) ? mifosCreditTransferResponse.getChanges().getCheckNumber() : "");
        mwr.setClientId((String.valueOf(mifosCreditTransferResponse.getClientId()) != null) ? String.valueOf(mifosCreditTransferResponse.getClientId()) : "");
        mwr.setOfficeId((String.valueOf(mifosCreditTransferResponse.getOfficeId()) != null) ? String.valueOf(mifosCreditTransferResponse.getOfficeId()) : "");
        mwr.setReferenceId(referenceId);

        mwr.setResourceId(String.valueOf(mifosCreditTransferResponse.getResourceId()));
        mwr.setRoutingCode(mifosCreditTransferResponse.getChanges().getRoutingCode());
        mwr.setSavingsId(mifosCreditTransferResponse.getSavingsId().toString());

        mwr.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .toString());

        mifosWithdrawalResponseRepo.save(mwr);

    }


    public MifosCreditTransferResponse postCreditTransferToMifos(
            ConfirmCreditTransferRequest confirmCreditTransferRequest,
            MifosCreditTransferResponse mifosWithdrawalResponse)

            throws CreditTransferValidationException, URISyntaxException {
        MifosCreditTransferResponse depositResponseEntityBody = null;
        try {

            logger.debug("================> Posting credit leg to mifos ================| ");


            final MifosDepositRequest mifosDepositRequest = customObjectMapper
                    .buildMifosDepositRequest(confirmCreditTransferRequest);

            URI uri = new URI(findMifosURLForCreditTransfer(getBeneficiaryAccountId(confirmCreditTransferRequest.getBeneficiaryAccountNo()),
                    confirmCreditTransferRequest.getBeneficiaryBankCode())); //getBeneficiaryAccountId method is needed cos mifos works with account id and only accountno of beneficiary is sent in from the client

            RequestEntity<MifosDepositRequest> request = RequestEntity
                    .post(uri)
                    .header("X-Mifosconfig-Platform-TenantID", "default")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mifosDepositRequest);

            final ResponseEntity<MifosCreditTransferResponse> mifosDepositResponseEntity = restTemplate
                    .exchange(request, MifosCreditTransferResponse.class);

            depositResponseEntityBody =
                    mifosDepositResponseEntity.getBody();

            if (mifosDepositResponseEntity.getStatusCodeValue() != 200) {
                throw new CreditTransferValidationException("Invalid request on sender's account");
            }


        } catch (HttpClientErrorException e) {
            //if something goes wrong with the crediting the beneficiary reverse the debit
            // leg by posting undo with the resourceid.
            undoMifosTransaction(mifosWithdrawalResponse.getResourceId(), mifosWithdrawalResponse.getSavingsId());
            throw new CreditTransferValidationException("Beneficiary account failed validation.");
        }


        return depositResponseEntityBody;

    }

    private int undoMifosTransaction(int resourceId, int accountId) {
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


    public String getBeneficiaryAccountId(String accountNo) {
        return accountRepo.findByAccountNo(accountNo).getAccountId();
    }

    public MifosCreditTransferResponse postDebitTransferToMifos(ConfirmCreditTransferRequest confirmCreditTransferRequest)
            throws CreditTransferValidationException, URISyntaxException {

        MifosCreditTransferResponse depositResponseEntityBody = null;
        final MifosDepositRequest mifosDepositRequest = customObjectMapper
                .buildMifosWithdrawalRequest(confirmCreditTransferRequest);

        URI uri = new URI(findMifosURLForDebitTransfer(confirmCreditTransferRequest.getSenderAccountId(),
                confirmCreditTransferRequest.getSenderBankCode()));

        RequestEntity<MifosDepositRequest> request = RequestEntity
                .post(uri)
                .header("X-Mifosconfig-Platform-TenantID", "default")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(mifosDepositRequest);

        final ResponseEntity<MifosCreditTransferResponse> mifosDepositResponseEntity = restTemplate
                .exchange(request, MifosCreditTransferResponse.class);

        depositResponseEntityBody =
                mifosDepositResponseEntity.getBody();

        if (mifosDepositResponseEntity.getStatusCodeValue() != 200) {
            throw new CreditTransferValidationException("Invalid request on sender's account");
        }


        return depositResponseEntityBody;

    }
}
