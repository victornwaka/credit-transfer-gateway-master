package net.arca.openbanking.credit_transfer.validation;


import net.arca.openbanking.credit_transfer.repository.AccountRepo;

public class AccountValidation {

    private AccountRepo accountRepo;

    public AccountValidation(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public boolean doesAccountIdExist(String accountId){

        if (accountRepo.findOne(accountId) == null) {
            return false;
        }else{
            return true;
        }
    }


    public boolean doesAccountNoExist(String accountNo){
        if(accountRepo.findByAccountNo(accountNo) == null){
            return false;
        }else{
            return true;
        }
    }

}
