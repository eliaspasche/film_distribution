package com.bbs.filmdistribution.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Create a password with hash and salt from a raw string input.
 */
public class PasswordConverter implements Converter<String, String>
{
    @Override
    public Result<String> convertToModel( String input, ValueContext valueContext )
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Result.ok( passwordEncoder.encode( input ) );
    }

    @Override
    public String convertToPresentation( String input, ValueContext valueContext )
    {
        return "";
    }
}
