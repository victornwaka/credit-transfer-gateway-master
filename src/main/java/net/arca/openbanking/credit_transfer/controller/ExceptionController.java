package net.arca.openbanking.credit_transfer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.arca.openbanking.credit_transfer.config.constants.Constants;
import net.arca.openbanking.credit_transfer.exception.*;
import net.arca.openbanking.credit_transfer.response.mifos_error.MifosErrorObject;
import com.arca.payload.commons.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<Error> uriSyntaxExceptionHandler(URISyntaxException e){
        return new ResponseEntity<Error>(new Error(Constants.HTTP_CLIENT_ERROR, e.getMessage()),
                HttpStatus.BAD_GATEWAY);
    }
    @ExceptionHandler(CreditTransferValidationException.class)
    public ResponseEntity<Error> accountEnquiryValidationExceptionHandler(CreditTransferValidationException e){
        return new ResponseEntity<Error>(new Error(Constants.HTTP_CLIENT_ERROR, e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Error> httpErrorHandler(HttpClientErrorException e){
        System.out.println("HttpClientErrorException ==> " + e.getResponseBodyAsString() + " =============|");

        MifosErrorObject mifosErrorObject = null;
        List<net.arca.openbanking.credit_transfer.response.mifos_error.Error> errorList = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            mifosErrorObject = objectMapper
                    .readValue(e.getResponseBodyAsString(), MifosErrorObject.class);

            System.out.println("Mifos Error object content ====> " + mifosErrorObject.toString() + " ===============|");
            errorList = mifosErrorObject.getErrors();
            if(errorList.size() != 0) {
                System.out.println("Default user message ==> " + errorList.get(0).getDefaultUserMessage() + " =============|");
            }else{
                System.out.println("**************** Error list is empty ***************");
            }

        } catch (IOException e1) {
            System.out.println("Error while mapping mifos error object ====> " + e1.getMessage());
            e1.printStackTrace();
        }
        return new ResponseEntity<Error>(new Error(String.valueOf(e.getStatusCode().value()), errorList.get(0)
                .getDefaultUserMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Error> AccountNotFoundErrorHandler(AccountNotFoundException d){
        return new ResponseEntity<Error>(new Error(Constants.ACCOUNT_NOT_FOUND, d.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Error> TransactionNotFoundErrorHandler(TransactionNotFoundException d){
        return new ResponseEntity<Error>(new Error(Constants.TRANSACTION_NOT_FOUND, d.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> UserNotFoundErrorHandler(UserNotFoundException d){
        return new ResponseEntity<Error>(new Error(Constants.USER_NOT_FOUND, d.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OTPMismatchException.class)
    public ResponseEntity<Error> OTPMismatchException(OTPMismatchException d){
        return new ResponseEntity<Error>(new Error(Constants.USER_NOT_FOUND, d.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> globalErrorHandler(Exception e){
        return new ResponseEntity<Error>(new Error(Constants.INTERNAL_SERVER_ERROR, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}