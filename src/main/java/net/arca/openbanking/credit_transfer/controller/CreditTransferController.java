package net.arca.openbanking.credit_transfer.controller;


import com.arca.payload.commons.response.Error;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.arca.openbanking.credit_transfer.exception.*;
import net.arca.openbanking.credit_transfer.request.ConfirmCreditTransferRequest;
import net.arca.openbanking.credit_transfer.request.CreditRefundRequest;
import net.arca.openbanking.credit_transfer.request.CreditTransferDetails;
import net.arca.openbanking.credit_transfer.response.CreditTransferResponse;
import net.arca.openbanking.credit_transfer.service.CreditTransferService;
import net.arca.openbanking.credit_transfer.service.ReverseFundService;
import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


@RestController
public class CreditTransferController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CreditTransferService creditTransferService;

    @Autowired
    ReverseFundService reverseFundService;

    @CrossOrigin
    @PostMapping("/v1/credit/transfer")
    @ApiOperation(value = "logCreditTransferDetails", notes = "Log Credit transfer details")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = Map.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @ResponseBody
    public Map<String, String> logCreditTransferDetails(@RequestBody CreditTransferDetails creditTransferRequest)
            throws CreditTransferValidationException, AccountNotFoundException, UserNotFoundException {
        logger.debug("Payload ==> " + creditTransferRequest.toString() + "<== Payload");

        Map<String, String> resultMap = new HashMap<>();

        final Long creditTransferTrxId = creditTransferService.logCreditTransferRequest(creditTransferRequest);

        logger.debug("credit transfer id == > " + creditTransferTrxId + " ===================|");

        if (creditTransferTrxId != null) {
            resultMap.put("transactionId", creditTransferTrxId.toString());
            resultMap.put("statusCode", HttpStatus.CREATED.toString());
        } else {
            resultMap.put("statusCode", HttpStatus.BAD_REQUEST.toString());
        }


        return resultMap;

    }


    @CrossOrigin
    @PostMapping("/v1/confirmation/request")
    @ApiOperation(value = "submitCreditTransferDetails", notes = "Confirm Credit transfer details")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = CreditTransferResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @ResponseBody
    public net.arca.openbanking.credit_transfer.response.CreditTransferResponse submitCreditTransferDetails(@RequestBody ConfirmCreditTransferRequest confirmCreditTransferRequest) throws URISyntaxException, OTPMismatchException, CreditTransferValidationException {
        logger.debug("CreditTransferDetails Payload ==> " + confirmCreditTransferRequest.toString() + " =============|");

        return creditTransferService.processCreditTransfer(confirmCreditTransferRequest);

    }


    @CrossOrigin
    @PostMapping("/v1/credit/refund")
    @ApiOperation(value = "reverseFundsTransfer", notes = "Reverse funds")
    @ApiResponses({
            @ApiResponse(code = 201, message = "OK", response = Map.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @ResponseBody
    public Map<String, String> reverseFundsTransfer(@RequestBody CreditRefundRequest creditRefundRequest)
            throws TransactionNotFoundException {
        logger.debug("reverse fund payload ===>  "  + creditRefundRequest.getReferenceId() + " =====================|");

        Map<String, String> resultMap = new HashMap<>();

        Long trxId = reverseFundService.reverseFund(creditRefundRequest.getReferenceId());

        if (trxId != null) {
            resultMap.put("transactionId", trxId.toString());
            resultMap.put("statusCode", HttpStatus.CREATED.toString());
        } else {
            resultMap.put("statusCode", HttpStatus.BAD_REQUEST.toString());
        }

        return resultMap;
    }

}
