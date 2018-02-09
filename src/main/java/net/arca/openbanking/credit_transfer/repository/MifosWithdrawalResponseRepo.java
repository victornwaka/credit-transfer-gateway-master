package net.arca.openbanking.credit_transfer.repository;

import net.arca.openbanking.credit_transfer.domain.MifosWithdrawalResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MifosWithdrawalResponseRepo extends CrudRepository<MifosWithdrawalResponse, String> {
    MifosWithdrawalResponse findByReferenceId(Long referenceId);
}
