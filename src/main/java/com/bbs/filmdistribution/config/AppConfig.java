package com.bbs.filmdistribution.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for the application.
 * Some variables are loaded from the application.properties file.
 */
@Configuration
public class AppConfig
{

    @Value( "${filmdistribution.version}" )
    private String version;

    public String getVersion()
    {
        return version;
    }
}
