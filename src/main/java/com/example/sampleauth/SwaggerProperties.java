package com.example.sampleauth;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private Info info;

    @Setter
    @Getter
    static class Info {

        private String apiTitle;

        private String apiDescription;

        private String apiVersion;

        private String apiContactName;

        private String apiContactEmail;

        private String apiContactUrl;

    }

}

/**/
