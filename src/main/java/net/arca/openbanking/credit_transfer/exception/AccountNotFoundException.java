package net.arca.openbanking.credit_transfer.exception;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
