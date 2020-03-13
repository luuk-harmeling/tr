package com.rockstars.rockstar.rockstar.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String SECURED_PATH = "/rest/**";

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint = new RestAuthenticationEntryPoint();

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.info("Basic Authentication enabled");

        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests().antMatchers(SECURED_PATH).authenticated();
        httpSecurity.httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception
    {
        auth.inMemoryAuthentication()
                .withUser("rockstar")
                .password("{noop}password")
                .roles("USER");
    }

}
