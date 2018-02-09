package net.arca.openbanking.credit_transfer.validation;

import java.util.Optional;

final class ValidationSupport {
    private static final ValidationResult valid = new ValidationResult(){
        public boolean isValid(){ return true; }
        public Optional<String> getReason(){ return Optional.empty(); }
    };

    static ValidationResult valid(){
        return valid;
    }
}
