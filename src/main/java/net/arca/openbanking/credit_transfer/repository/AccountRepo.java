package net.arca.openbanking.credit_transfer.repository;


import net.arca.openbanking.credit_transfer.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends CrudRepository<Account, String>{
     Account findByAccountNo(String accountNo);
}
