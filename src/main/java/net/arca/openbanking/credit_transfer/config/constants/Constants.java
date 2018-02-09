package net.arca.openbanking.credit_transfer.config.constants;

public class Constants {
    public static final String USER_NOT_FOUND = "User not found.";

    private Constants() { }
    public static final String HTTP_CLIENT_ERROR = "01";
    public static final String ACCOUNT_NOT_FOUND = "02";
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String TRANSACTION_NOT_FOUND = "03";

    public static final String RequestId_is_empty = "RequestId is empty.";
    public static final String Account_holder_is_empty =  "Account holder is empty.";
    public static final String Bank_code_is_empty = "Bank code is empty.";
    public static final String Invalid_trx_date = "Invalid transaction date; correct format (yyyy-MM-dd HH:mm:ss)";
    public static final String Bank_code_is_invalid = "Bank code is invalid.";
    public static final String Email_is_invalid = "Email id is invalid.";
    public static final String Invalid_charge_bearer = "Invalid charge bearer.";
    public static final String Invalid_account_no = "Invalid account number.";
    public static final String Invalid_currency = "Invalid currency.";
}