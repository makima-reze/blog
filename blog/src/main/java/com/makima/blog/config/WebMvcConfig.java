package com.makima.blog.config;



import com.makima.blog.handler.PageableHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web mvc配置
 *
 * @author yezhiqiu
 * @date 2021/07/29
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Bean
//    public WebSecurityHandler getWebSecurityHandler() {
//        return new WebSecurityHandler();
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("*")
                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PageableHandlerInterceptor());
//        registry.addInterceptor(getWebSecurityHandler());
    }

    //设置上传图片的路径地址，前面设置的数据库保存的路径，后面是真实去寻找的路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:/usr/local/upload/");
    }
}
