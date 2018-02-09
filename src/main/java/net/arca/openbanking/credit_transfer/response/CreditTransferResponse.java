package net.arca.openbanking.credit_transfer.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "transactionId",
        "referenceId",
        "errorCode",
        "errorMessage"

})
public class CreditTransferResponse {
    @JsonProperty("transactionId")
    private Long transactionId;
    @JsonProperty("referenceId")
    private Long referenceId;
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("errorMessage")
    private String errorMessage;
}
