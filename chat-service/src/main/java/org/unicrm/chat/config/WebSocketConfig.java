package org.unicrm.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker( "/topic","/queue");
        config.setApplicationDestinationPrefixes("/chat");
        config.setUserDestinationPrefix("/user");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setHandshakeHandler(new DefaultHandshakeHandler(){
                    public boolean beforeHandshake(
                            ServerHttpRequest request,
                            ServerHttpResponse response,
                            WebSocketHandler wsHandler,
                            Map attributes) throws Exception {

                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest
                                    = (ServletServerHttpRequest) request;
                            HttpSession session = servletRequest
                                    .getServletRequest().getSession();
                            attributes.put("sessionId", session.getId());
                        }
                        return true;
                    }})
                .setAllowedOriginPatterns("*")

                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);

    }

    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }
}