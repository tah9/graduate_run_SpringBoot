package com.tah.graduate_run.config;

/**
 * ->  tah9  2021/8/28 15:07
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Value("${face-file-path}")
    private String faceFilePath;
    @Value("${emoji-file-path}")
    private String emojiFilePath;
    @Value("${articlepics-path}")
    private String articlepicsPath;
    @Value("${box-path}")
    private String boxPath;
    @Value("${root-path}")
    private String rootPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 配置资源映射
         * 如果访问的资源路径是以“/graduate/face/”开头的，
         * 就给我映射到faceFilePath，去找你要的资源
         */
        registry.addResourceHandler("/graduate/**")
                .addResourceLocations("file:" + faceFilePath)
                .addResourceLocations("file:" + emojiFilePath)
                .addResourceLocations("file:" + articlepicsPath)
                .addResourceLocations("file:" + boxPath)
                .addResourceLocations("file:" + rootPath);
    }

//    /**
//     *  允许跨域访问
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // 可限制哪个请求可以通过跨域
//                .allowedHeaders("*")  // 可限制固定请求头可以通过跨域
//                .allowedMethods("*") // 可限制固定methods可以通过跨域
//                .allowedOrigins("*")  // 可限制访问ip可以通过跨域
////                .allowCredentials(true) // 是否允许发送cookie
//                .exposedHeaders(HttpHeaders.SET_COOKIE);
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                //添加拦截路径
                .addPathPatterns("/**")
                //添加白名单路径
                .excludePathPatterns("/swagger-resources/**");
    }

    /**
     * 全局注入拦截器配置Bean
     *
     * @return
     */
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}

