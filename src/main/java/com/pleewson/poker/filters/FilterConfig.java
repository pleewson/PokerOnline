package com.pleewson.poker.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserSessionFilter> userSessionFilterFilter(){
        FilterRegistrationBean<UserSessionFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new UserSessionFilter());
        registrationBean.addUrlPatterns("/*"); //apply to all URLs

        return registrationBean;
    }
}
