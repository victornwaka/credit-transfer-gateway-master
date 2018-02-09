package net.arca.openbanking.credit_transfer.repository;

import net.arca.openbanking.credit_transfer.domain.MifosDepositResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MifosDepositResponseRepo extends CrudRepository<MifosDepositResponse, Long>{
    MifosDepositResponse findByReferenceId(Long referenceId);
}
