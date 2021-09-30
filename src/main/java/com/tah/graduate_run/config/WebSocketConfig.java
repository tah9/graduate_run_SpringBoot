package com.tah.graduate_run.config;

/**
 * ->  tah9  2021/9/9 14:04
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return  new ServerEndpointExporter();
    }
}
