package net.arca.openbanking.credit_transfer.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "senderAccountId",
        "senderAccountNo",
        "senderAccountName",
        "senderBankCode",
        "amount",
        "remarks",
        "beneficiaryAccountNo",
        "beneficiaryAccountName",
        "beneficiaryBankCode",
        "currency"
})
public class CreditTransferDetails {
    @ApiModelProperty(required = true)
    @JsonProperty("username")
    private String username;
    @ApiModelProperty(required = true)
    @JsonProperty("senderAccountId")
    private String senderAccountId;
    @ApiModelProperty(required = true)
    @JsonProperty("senderAccountNo")
    private String senderAccountNo;
    @ApiModelProperty(required = true)
    @JsonProperty("senderAccountName")
    private String senderAccountName;
    @ApiModelProperty(required = true, example = "028")
    @JsonProperty("senderBankCode")
    private String senderBankCode;
    @ApiModelProperty(required = true)
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("remarks")
    private String remarks;
    @ApiModelProperty(required = true)
    @JsonProperty("beneficiaryAccountNo")
    private String beneficiaryAccountNo;
    @ApiModelProperty(required = true)
    @JsonProperty("beneficiaryAccountName")
    private String beneficiaryAccountName;
    @ApiModelProperty(required = true, example = "028")
    @JsonProperty("beneficiaryBankCode")
    private String beneficiaryBankCode;
    @ApiModelProperty(required = true, example = "NGN")
    @JsonProperty("currency")
    private String currency;


}
