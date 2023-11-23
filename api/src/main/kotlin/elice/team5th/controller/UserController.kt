package elice.team5th.controller

import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
    @Operation(summary = "유저 목록 조회", description = "유저 목록 페이징 조회합니다. 밴 여부, 닉네임 prefix로 필터링할 수 있습니다.")
    @GetMapping("/")
    fun getUsers(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "20") limit: Int,
        @Parameter(description = "밴 여부", example = "true/false")
        @RequestParam(value = "ban", defaultValue = "false", required = false)
        ban: Boolean,
        @Parameter(description = "닉네임 prefix", example = "김")
        @RequestParam(value = "nickname_prefix", required = false)
        nicknamePrefix: String?
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

    @Operation(summary = "단일 유저 조회", description = "유저 아이디를 통해 유저를 조회합니다.")
    @GetMapping("/{userId}")
    fun getUser(
        @Parameter(description = "user_id", example = "117452459527937826982") @PathVariable userId: String
    ): ResponseEntity<UserDto.Response> {
        val user = userService.findUserById(userId)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @Operation(summary = "유저 정보 수정", description = "유저 아이디를 통해 유저 정보를 수정합니다.")
    @PutMapping("/{userId}")
    fun updateUser(
        @Parameter(description = "user_id", example = "117452459527937826982") @PathVariable userId: String,
        @RequestBody updateRequest: UserDto.UpdateRequest
    ): ResponseEntity<UserDto.Response> {
        val user = userService.updateUser(userId, updateRequest)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }
}
