
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
    "officeId",
    "clientId",
    "savingsId",
    "resourceId",
    "changes"
})
public class MifosCreditTransferResponse {

    @JsonProperty("officeId")
    private Integer officeId;
    @JsonProperty("clientId")
    private Integer clientId;
    @JsonProperty("savingsId")
    private Integer savingsId;
    @JsonProperty("resourceId")
    private Integer resourceId;
    @JsonProperty("changes")
    private Changes changes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("officeId")
    public Integer getOfficeId() {
        return officeId;
    }

    @JsonProperty("officeId")
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    @JsonProperty("clientId")
    public Integer getClientId() {
        return clientId;
    }

    @JsonProperty("clientId")
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    @JsonProperty("savingsId")
    public Integer getSavingsId() {
        return savingsId;
    }

    @JsonProperty("savingsId")
    public void setSavingsId(Integer savingsId) {
        this.savingsId = savingsId;
    }

    @JsonProperty("resourceId")
    public Integer getResourceId() {
        return resourceId;
    }

    @JsonProperty("resourceId")
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @JsonProperty("changes")
    public Changes getChanges() {
        return changes;
    }

    @JsonProperty("changes")
    public void setChanges(Changes changes) {
        this.changes = changes;
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
