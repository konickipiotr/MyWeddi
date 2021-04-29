package com.myweddi.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/photos/**")
                .addResourceLocations("file://" + Global.appPath + Global.photosPath);
        registry
                .addResourceHandler("/church/**")
                .addResourceLocations("file://" + Global.appPath + "church/");
        registry
                .addResourceHandler("/weddinghouse/**")
                .addResourceLocations("file://" + Global.appPath + "weddinghouse/");
        registry
                .addResourceHandler("/tables/**")
                .addResourceLocations("file://" + Global.appPath + "tables/");
        registry
                .addResourceHandler("/profilephoto/**")
                .addResourceLocations("file://" + Global.appPath + "profilephoto/");
    }
}
