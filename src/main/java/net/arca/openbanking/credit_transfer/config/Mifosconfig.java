package net.arca.openbanking.credit_transfer.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@ToString
@Configuration
@ConfigurationProperties(prefix = "mifosconfig")
public class Mifosconfig {
    private String instance1ip;
    private String instance2ip;
    private String port;
    private String instancecode1;
    private String instancecode2;
    private String switchcredittransferuri;
}