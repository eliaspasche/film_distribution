package com.bbs.filmdistribution.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for the application.
 * Some variables are loaded from the application.yml/application-local.yml file.
 */
@Getter
@Configuration
public class AppConfig
{

    public static final String AUTO_LOGIN_KEY = "AutoLogin";

    @Value( "${filmdistribution.version}" )
    private String version;

    @Value( "${filmdistribution.autologin}" )
    private boolean autoLogin;

    @Value( "${filmdistribution.pdf-save-path}" )
    private String pdfSavePath;
}
