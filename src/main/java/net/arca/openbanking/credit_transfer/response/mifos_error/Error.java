
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
    "defaultUserMessage",
    "userMessageGlobalisationCode",
    "parameterName",
    "value",
    "args"
})
public class Error {

    @JsonProperty("developerMessage")
    private String developerMessage;
    @JsonProperty("defaultUserMessage")
    private String defaultUserMessage;
    @JsonProperty("userMessageGlobalisationCode")
    private String userMessageGlobalisationCode;
    @JsonProperty("parameterName")
    private String parameterName;
    @JsonProperty("value")
    private Object value;
    @JsonProperty("args")
    private List<Arg> args = null;
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

    @JsonProperty("parameterName")
    public String getParameterName() {
        return parameterName;
    }

    @JsonProperty("parameterName")
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @JsonProperty("value")
    public Object getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Object value) {
        this.value = value;
    }

    @JsonProperty("args")
    public List<Arg> getArgs() {
        return args;
    }

    @JsonProperty("args")
    public void setArgs(List<Arg> args) {
        this.args = args;
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
