package elice.team5th.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // ws -> http, wss -> https
        registry.addEndpoint("/ws").setAllowedOrigins("*") // chat: endpoint
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic", "/user")  // topic: broadcast, user: private
        registry.setApplicationDestinationPrefixes("/app")  // app: prefix for message
        registry.setUserDestinationPrefix("/user")  // user: prefix for private message
    }
}
