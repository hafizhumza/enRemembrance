package com.en.remembrance.utilities;

import com.en.remembrance.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class UrlBuilderUtil {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public UrlBuilderUtil(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public UriComponentsBuilder getApplicationUri() {
        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .scheme(applicationProperties.getApplicationScheme())
                .host(applicationProperties.getApplicationHost());

        if (StringUtils.hasText(applicationProperties.getApplicationPort())) {
            uriBuilder.port(applicationProperties.getApplicationPort());
        }

        return uriBuilder;
    }

    public String createUrlWithToken(String pathUrl, String token) {
        return getApplicationUri()
                .path(pathUrl)
                .path(token)
                .toUriString();
    }

}
