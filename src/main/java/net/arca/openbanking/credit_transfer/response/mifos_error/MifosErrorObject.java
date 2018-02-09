
package net.arca.openbanking.credit_transfer.response.mifos_error;

import com.fasterxml.jackson.annotation.*;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "developerMessage",
    "httpStatusCode",
    "defaultUserMessage",
    "userMessageGlobalisationCode",
    "errors"
})
public class MifosErrorObject {

    @JsonProperty("developerMessage")
    private String developerMessage;
    @JsonProperty("httpStatusCode")
    private String httpStatusCode;
    @JsonProperty("defaultUserMessage")
    private String defaultUserMessage;
    @JsonProperty("userMessageGlobalisationCode")
    private String userMessageGlobalisationCode;
    @JsonProperty("errors")
    private List<net.arca.openbanking.credit_transfer.response.mifos_error.Error> errors = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("developerMessage")
    public String getDeveloperMessage() {
        return developerMessage;
    }

    @JsonProperty("developerMessage")
    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    @JsonProperty("httpStatusCode")
    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    @JsonProperty("httpStatusCode")
    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @JsonProperty("defaultUserMessage")
    public String getDefaultUserMessage() {
        return defaultUserMessage;
    }

    @JsonProperty("defaultUserMessage")
    public void setDefaultUserMessage(String defaultUserMessage) {
        this.defaultUserMessage = defaultUserMessage;
    }

    @JsonProperty("userMessageGlobalisationCode")
    public String getUserMessageGlobalisationCode() {
        return userMessageGlobalisationCode;
    }

    @JsonProperty("userMessageGlobalisationCode")
    public void setUserMessageGlobalisationCode(String userMessageGlobalisationCode) {
        this.userMessageGlobalisationCode = userMessageGlobalisationCode;
    }

    @JsonProperty("errors")
    public List<net.arca.openbanking.credit_transfer.response.mifos_error.Error> getErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(List<Error> errors) {
        this.errors = errors;
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
