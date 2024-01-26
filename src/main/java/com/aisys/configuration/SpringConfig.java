package com.aisys.configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

public class SpringConfig extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**").addResourceLocations("/resource/")
                .setCachePeriod(86400)
                .resourceChain(true)
                .addResolver(new GzipResourceResolver())
                .addResolver(new PathResourceResolver());
    }
}
