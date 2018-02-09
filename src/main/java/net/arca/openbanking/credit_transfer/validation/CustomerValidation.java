package net.arca.openbanking.credit_transfer.validation;

import net.arca.openbanking.credit_transfer.repository.UserRepo;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomerValidation {

    private UserRepo userRepo;

    public CustomerValidation(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean doesUserExist(String username){
        if(userRepo.findOne(username) == null){
            return false;
        }else{
            return true;
        }

    }
}
