package com.whut.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.util.logging.Logger;

@Configuration
public class StaticResourceConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Logger logger = Logger.getLogger("StaticResourceConfig");

        File file = new File("/var/download");
        if (!file.exists()){
            boolean res = file.mkdir();
            logger.info("创建/var/download文件夹:"+res);
        }
        logger.info("文件夹/var/download已存在");
        registry.addResourceHandler("/download/**").addResourceLocations("file:/var/download/");
        super.addResourceHandlers(registry);
    }
}