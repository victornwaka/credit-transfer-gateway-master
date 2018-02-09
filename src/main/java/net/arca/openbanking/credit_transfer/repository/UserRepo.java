package net.arca.openbanking.credit_transfer.repository;

import net.arca.openbanking.credit_transfer.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, String>{
}
