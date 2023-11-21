package elice.team5th.controller

import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val tokenProvider: AuthTokenProvider
) {
    @GetMapping("/")
    fun getUsers(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "20") limit: Int,
        @RequestParam(value = "ban", defaultValue = "false", required = false) ban: Boolean,
        @RequestParam(value = "nickname_prefix", required = false) nicknamePrefix: String?
    ): ResponseEntity<Page<UserDto.Response>> {
        val pageRequest = PageRequest.of(offset, limit)
        val users = when {
            ban && nicknamePrefix != null -> userService.findUsersByNicknameStartingWithAndBanIsTrue(
                pageRequest,
                nicknamePrefix
            )
            ban -> userService.findUsersByBanIsTrue(pageRequest)
            nicknamePrefix != null -> userService.findUsersByNicknameStartingWith(pageRequest, nicknamePrefix)
            else -> userService.findAllUsers(pageRequest)
        }
        return ResponseEntity.ok().body(users.map { UserDto.Response(it) })
    }

//    @GetMapping("/me")
//    fun getMe(
//        @CurrentUser user: User
//    ): ResponseEntity<UserDto.Response> {
//        return ResponseEntity.ok().body(UserDto.Response(user))
//    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: String
    ): ResponseEntity<UserDto.Response> {
        val user = userService.findUserById(userId)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: String,
        @RequestBody updateRequest: UserDto.UpdateRequest
    ): ResponseEntity<UserDto.Response> {
        val user = userService.updateUser(userId, updateRequest)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }
}
