package elice.team5th.controller

import elice.team5th.domain.auth.annotation.CurrentUser
import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.model.User
import elice.team5th.domain.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
    @GetMapping
    fun getUsers(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "20") limit: Int
    ): ResponseEntity<Page<UserDto.Response>> {
        val users = userService.findAllUsers(PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(users.map { UserDto.Response(it) })
    }

    @GetMapping("/me")
    fun getMe(
        @CurrentUser user: User
    ): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: String
    ): ResponseEntity<UserDto.Response> {
        val user = userService.findUserById(userId)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @GetMapping("/search/nickname")
    fun getUserByNickname(
        @RequestParam(value = "nickname") nickname: String
    ): ResponseEntity<UserDto.Response> {
        val user = userService.findUserByNickname(nickname)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    // Todo: 현재 로그인 user @CurrentUser 로 받기
    @PostMapping("/{userId}")
    fun updateProfile(
        @PathVariable userId: String,
        @RequestBody updateRequest: UserDto.UpdateProfileRequest
    ): ResponseEntity<UserDto.Response> {
        val user = userService.updateProfile(userId, updateRequest)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @GetMapping("/search/ban")
    fun getBannedUsers(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "20") limit: Int
    ): ResponseEntity<Page<UserDto.Response>> {
        val users = userService.findUsersByBanIsTrue(PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(users.map { UserDto.Response(it) })
    }

    @PutMapping("/{userId}/ban")
    fun banUser(
        @PathVariable userId: String
    ): ResponseEntity<UserDto.Response> {
        val user = userService.banUser(userId)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }
}
