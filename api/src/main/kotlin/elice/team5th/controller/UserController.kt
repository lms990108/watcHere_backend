package elice.team5th.controller

import elice.team5th.domain.auth.annotation.CurrentUser
import elice.team5th.domain.auth.entity.UserPrincipal
import elice.team5th.domain.auth.token.AuthTokenProvider
import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val tokenProvider: AuthTokenProvider
) {
    @Operation(summary = "유저 목록 조회", description = "유저 목록 페이징 조회합니다. 밴 여부, 닉네임 prefix로 필터링할 수 있습니다.")
    @GetMapping("/admin")
    fun getUsers(
        @CurrentUser userPrincipal: UserPrincipal,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "20") limit: Int,
        @Parameter(description = "밴 여부", example = "true/false")
        @RequestParam(value = "ban", defaultValue = "false", required = false)
        ban: Boolean,
        @Parameter(description = "닉네임 prefix", example = "김")
        @RequestParam(value = "nickname_prefix", required = false)
        nicknamePrefix: String?
    ): ResponseEntity<Page<UserDto.Response>> {
        val pageRequestSortByUpdatedAt = PageRequest.of(offset, limit, Sort.by("updatedAt").descending())
        val pageRequest = PageRequest.of(offset, limit)

        val users = when {
            ban && nicknamePrefix != null -> userService.findUsersByNicknameStartingWithAndBanIsTrue(
                pageRequest,
                nicknamePrefix
            )
            ban -> userService.findUsersByBanIsTrue(pageRequestSortByUpdatedAt)
            nicknamePrefix != null -> userService.findUsersByNicknameStartingWith(pageRequest, nicknamePrefix)
            else -> userService.findAllUsers(pageRequestSortByUpdatedAt)
        }
        return ResponseEntity.ok().body(users.map { UserDto.Response(it) })
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 유저의 정보를 조회합니다.")
    @GetMapping("/me")
    fun getMe(@CurrentUser userPrincipal: UserPrincipal): ResponseEntity<UserDto.Response> {
        val user = userService.findUserById(userPrincipal.userId)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @Operation(summary = "단일 유저 조회", description = "유저 아이디를 통해 유저를 조회합니다.")
    @GetMapping("/admin/{userId}")
    fun getUser(
        @CurrentUser userPrincipal: UserPrincipal,
        @Parameter(description = "user_id", example = "117452459527937826982") @PathVariable userId: String
    ): ResponseEntity<UserDto.Response> {
        val user = userService.findUserById(userId)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @Operation(summary = "유저 정보 수정", description = "유저 아이디를 통해 유저 정보(닉네임, 프로필 사진)를 수정합니다.")
    @PutMapping("/me", consumes = ["multipart/form-data"])
    fun updateUser(
        @CurrentUser userPrincipal: UserPrincipal,
        @Parameter(name = "nickname", description = "닉네임")
        @RequestParam(value = "nickname", required = false)
        nickname: String?,
        @Parameter(name = "poster", description = "포스터 이미지 링크")
        @RequestParam(value = "poster", required = false) poster: String?,
        @Parameter(
            name = "profile_image",
            description = "multipart/form-data 형식의 사진 파일을 input으로 받습니다.",
            example = "image.png"
        )
        @RequestPart(value = "profile_image", required = false)
        profileImage: MultipartFile?
    ): ResponseEntity<UserDto.Response> {
        val user = userService.updateUser(userPrincipal.userId, nickname, poster, profileImage)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @Operation(summary = "유저 밴 여부 수정", description = "유저 아이디를 통해 유저의 밴 여부를 수정합니다.")
    @PutMapping("/admin/ban")
    fun updateBan(
        @Parameter(description = "현재 로그인한 유저") @CurrentUser userPrincipal: UserPrincipal,
        @Parameter(description = "대상이 되는 유저 아이디")
        @RequestParam(value = "user_id", required = true)
        userId: String,
        @RequestParam(value = "ban", defaultValue = "false", required = true) ban: Boolean
    ): ResponseEntity<UserDto.Response> {
        val user = userService.updateBan(userId, ban)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @Operation(summary = "유저 권한 수정", description = "유저 아이디를 통해 유저의 권한을 수정합니다.")
    @PutMapping("/admin/role")
    fun updateRole(
        @Parameter(description = "현재 로그인한 유저") @CurrentUser userPrincipal: UserPrincipal,
        @Parameter(description = "대상이 되는 유저 아이디")
        @RequestParam(value = "user_id", required = true)
        userId: String,
        @Parameter(description = "role", example = "USER, ADMIN, GUEST")
        @RequestParam(value = "role", required = true)
        role: RoleType
    ): ResponseEntity<UserDto.Response> {
        val user = userService.updateRole(userId, role)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @Operation(
        summary = "유저 탈퇴",
        description = "접속한 유저가 탈퇴 요청을 보내면 탈퇴합니다. " +
            "실제로 삭제되는 것이 아닌 deleted_at 컬럼에 현재 시간이 저장됩니다. 토큰은 만료됩니다."
    )
    @DeleteMapping("/me")
    fun deleteUser(@CurrentUser userPrincipal: UserPrincipal): ResponseEntity<String> {
        userService.deleteUser(userPrincipal.userId)
        return ResponseEntity.ok().body("탈퇴되었습니다.")
    }

    @Operation(summary = "로그아웃", description = "접속한 유저의 토큰을 만료시킵니다.")
    @PostMapping("/logout")
    fun logout(@CurrentUser userPrincipal: UserPrincipal): ResponseEntity<String> {
        userService.logout(userPrincipal.userId)
        return ResponseEntity.ok().body("로그아웃되었습니다.")
    }
}
