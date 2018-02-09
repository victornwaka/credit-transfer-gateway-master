
package net.arca.openbanking.credit_transfer.response;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "accountNumber",
    "checkNumber",
    "routingCode",
    "receiptNumber",
    "bankNumber"
})
public class Changes {

    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("checkNumber")
    private String checkNumber;
    @JsonProperty("routingCode")
    private String routingCode;
    @JsonProperty("receiptNumber")
    private String receiptNumber;
    @JsonProperty("bankNumber")
    private String bankNumber;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("checkNumber")
    public String getCheckNumber() {
        return checkNumber;
    }

    @JsonProperty("checkNumber")
    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    @JsonProperty("routingCode")
    public String getRoutingCode() {
        return routingCode;
    }

    @JsonProperty("routingCode")
    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    @JsonProperty("receiptNumber")
    public String getReceiptNumber() {
        return receiptNumber;
    }

    @JsonProperty("receiptNumber")
    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @JsonProperty("bankNumber")
    public String getBankNumber() {
        return bankNumber;
    }

    @JsonProperty("bankNumber")
    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
