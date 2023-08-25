package com.bbs.filmdistribution.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
