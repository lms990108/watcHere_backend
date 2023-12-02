package elice.team5th.domain.user.dto

import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

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

        @Schema(
            description = "poster image link",
            example = "https://lh3.googleusercontent.com/a/ACg8ocIqCt6pUKU2p4K7kU3zLTgzyqkznfaW2EgC-M97DCpE6A=s96-c"
        )
        val poster: String,

        @Schema(description = "nickname", example = "김코딩")
        val nickname: String,

        @Schema(description = "ban 여부", example = "false")
        val ban: Boolean,

        @Schema(description = "provider", example = "GOOGLE/NAVER/KAKAO")
        val provider: ProviderType,

        @Schema(description = "role", example = "USER/ADMIN/GUEST")
        val role: RoleType,

        @Schema(description = "created_at", example = "2021-08-23T14:00:00")
        val createdAt: LocalDateTime?,

        @Schema(description = "updated_at", example = "2021-08-23T14:00:00")
        val updatedAt: LocalDateTime?
    ) {
        constructor(user: User) : this(
            userId = user.userId,
            email = user.email ?: "",
            profileImage = user.profileImage,
            poster = user.poster ?: "",
            nickname = user.nickname,
            ban = user.ban,
            provider = user.provider,
            role = user.role,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
