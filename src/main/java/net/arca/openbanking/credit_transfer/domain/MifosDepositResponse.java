package net.arca.openbanking.credit_transfer.domain;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "mifos_deposit_response")
public class MifosDepositResponse {

    @Column(name = "office_id")
    private String officeId;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "savings_id")
    private String savingsId;
    @Id
    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "check_number")
    private String checkNumber;
    @Column(name = "routing_code")
    private String routingCode;
    @Column(name = "bank_number")
    private String bankNumber;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "reference_id")
    private long referenceId;
}
