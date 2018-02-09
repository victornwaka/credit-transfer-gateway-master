package net.arca.openbanking.credit_transfer.validation;

import java.util.Optional;

public interface ValidationResult {
    static ValidationResult valid(){
        return ValidationSupport.valid();
    }

    static ValidationResult invalid(String reason){
        return new Invalid(reason);
    }

    boolean isValid();

    Optional<String> getReason();
}