package elice.team5th.config

import elice.team5th.domain.chat.model.Message
import elice.team5th.domain.chat.model.MessageType
import elice.team5th.domain.user.service.UserService
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener(
    private val userService: UserService,
    private val messageTemplate: SimpMessageSendingOperations
) {
    val log = logger()

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as String
        val user = userService.findUserByNickname(username)

        log.info("User Disconnected : $username")

        val message = Message(user, null, MessageType.LEAVE)
        messageTemplate.convertAndSend("/topic/public", message)
    }
}
