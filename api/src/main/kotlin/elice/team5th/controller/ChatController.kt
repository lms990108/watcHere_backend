package elice.team5th.controller

import elice.team5th.domain.chat.model.Message
import elice.team5th.domain.user.service.UserService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class ChatController(
    private val userService: UserService,

) {
    @MessageMapping("/chat.sendMessage")  // 클라이언트가 /chat.sendMessage로 메시지를 보내면
    @SendTo("/topic/public")  // /topic/public queue로 메시지를 보낸다.
    fun send(@Payload message: Message): Message {
        return message
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    fun addUser(@Payload message: Message, headerAccessor: SimpMessageHeaderAccessor): Message {
        // Add username in web socket session
        headerAccessor.sessionAttributes?.put("username", message.user.nickname)
        return message
    }
}
