package com.en.remembrance.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "settings", ignoreUnknownFields = false)
public class ApplicationProperties {

    private boolean isDevMode;

    private String savedImagesDir;

    private String applicationScheme;

    private String applicationHost;

    private String applicationPort;

    SecurityProperties security = new SecurityProperties();

    public boolean isDevMode() {
        return isDevMode;
    }

    public void setIsDevMode(boolean isDevMode) {
        this.isDevMode = isDevMode;
    }

    public SecurityProperties security() {
        return security;
    }

}
