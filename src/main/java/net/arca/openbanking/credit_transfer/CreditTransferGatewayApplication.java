package net.arca.openbanking.credit_transfer;

import com.google.common.base.Predicate;
import groovy.lang.MetaClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
@Configuration
public class CreditTransferGatewayApplication {

    static {
        System.setProperty("javax.net.ssl.keyStore", "/app/mifos.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "/app/mifos-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperty("javax.net.debug", "ssl");
        System.setProperty("javax.net.ssl.keyStoreAlias", "tomcat");
    }


    public static void main(String[] args) {
        SpringApplication.run(CreditTransferGatewayApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors()
                .add(
                        new BasicAuthorizationInterceptor("mifos", "password")
                );

        return restTemplate;
    }

    @Bean
    public HttpHeaders header() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Mifosconfig-Platform-TenantID", "default");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build()
                .ignoredParameterTypes(MetaClass.class);

    }

    @Bean
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Open Banking API")
                .description("Bank APIs Gateway")
                .termsOfServiceUrl("")
                .license("")
                .licenseUrl("")
                .version("1.0")
                .build();
    }

    //Here is an example where we select any api that matches one of these paths
    private static Predicate<String> paths() {
        return or(
                regex("/v1/.*")
        );
    }

}
