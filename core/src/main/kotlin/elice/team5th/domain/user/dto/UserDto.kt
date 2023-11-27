package elice.team5th.domain.user.dto

import elice.team5th.domain.review.model.Review
import elice.team5th.domain.tmdb.entity.Content
import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

class UserDto {
    data class Response(
        @Schema(description = "user ID", example = "117452459527937826982")
        val userId: String?,

        @Schema(description = "email", example = "example@example.com")
        val email: String,

        @Schema(
            description = "profile image link",
            example = "https://lh3.googleusercontent.com/a/ACg8ocIqCt6pUKU2p4K7kU3zLTgzyqkznfaW2EgC-M97DCpE6A=s96-c"
        )
        val profileImage: String,

        @Schema(description = "nickname", example = "김코딩")
        val nickname: String,

        @Schema(description = "ban 여부", example = "false")
        val ban: Boolean,

        @Schema(description = "provider", example = "GOOGLE/NAVER/KAKAO")
        val provider: ProviderType,

        @Schema(description = "role", example = "USER/ADMIN/GUEST")
        val role: RoleType,
    ) {
        constructor(user: User) : this(
            userId = user.userId,
            email = user.email ?: "",
            profileImage = user.profileImage,
            nickname = user.nickname,
            ban = user.ban,
            provider = user.provider,
            role = user.role,
        )
    }
}
