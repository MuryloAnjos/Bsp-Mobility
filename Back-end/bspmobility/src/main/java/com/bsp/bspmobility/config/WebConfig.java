
package com.bsp.bspmobility.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imagens/**")
                .addResourceLocations("file:/var/imagens/"); // Linux/Mac
                // .addResourceLocations("file:///C:/imagens/"); // Windows
    }
}