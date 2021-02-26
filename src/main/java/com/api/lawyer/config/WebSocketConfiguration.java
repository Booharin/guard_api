package com.api.lawyer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
        //config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect").setAllowedOrigins("*");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(20000000);
    }

    /**
     *  @Override
     *     public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
     *         DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
     *         resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
     *         MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
     *         converter.setObjectMapper(new ObjectMapper());
     *         converter.setContentTypeResolver(resolver);
     *         messageConverters.add(converter);
     *         return false;
     *     }
     */
}