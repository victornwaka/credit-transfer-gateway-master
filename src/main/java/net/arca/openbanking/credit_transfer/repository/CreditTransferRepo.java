package net.arca.openbanking.credit_transfer.repository;

import net.arca.openbanking.credit_transfer.domain.CreditTransferDetails;
import net.arca.openbanking.credit_transfer.enums.TransactionStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CreditTransferRepo extends CrudRepository<CreditTransferDetails, Long> {

    @Query("select c from CreditTransferDetails c where c.transactionId = :transactionId and c.status = :status")
    CreditTransferDetails findByStatus(@Param("transactionId") long transactionId,
                                       @Param("status") TransactionStatus status);

    @Modifying
    @Query("UPDATE CreditTransferDetails c set c.status = :status, c.referenceId = :referenceId WHERE c.transactionId = :transactionId")
    void updateCreditTransferDetails(@Param("status") TransactionStatus status,
                                     @Param("referenceId") long referenceId,
                                     @Param("transactionId") long transactionId);

    @Query("select c from CreditTransferDetails c where c.referenceId = :referenceId and c.status = :status")
    CreditTransferDetails findByReferenceId(@Param("referenceId") long referenceId,
                                            @Param("status") TransactionStatus status);
}
