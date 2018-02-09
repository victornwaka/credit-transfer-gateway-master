package net.arca.openbanking.credit_transfer.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.arca.openbanking.credit_transfer.enums.TransactionStatus;

import javax.persistence.*;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "credit_transfer")
public class CreditTransferDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "trx_id")
    private long transactionId;
    private String username;
    @Column(name = "sender_account_id")
    private String senderAccountId;
    @Column(name = "sender_account_no")
    private String senderAccountNo;
    @Column(name = "sender_account_name")
    private String senderAccountName;
    @Column(name = "sender_bank_code")
    private String senderBankCode;
    private double amount;
    private String remarks;
    @Column(name = "beneficiary_account_no")
    private String beneficiaryAccountNo;
    @Column(name = "beneficiary_account_name")
    private String beneficiaryAccountName;
    @Column(name = "beneficiary_bank_code")
    private String beneficiaryBankCode;
    @Column(name = "trx_date")
    private String transactionDate;
    @Column(name = "otp")
    private int OTP;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;
    @Column(name = "reference_id")
    private Long referenceId;

}
