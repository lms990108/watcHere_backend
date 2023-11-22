package elice.team5th.domain.auth.repository

import elice.team5th.domain.auth.token.UserRefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface UserRefreshTokenRepository : JpaRepository<UserRefreshToken, Long> {
    fun findByUserId(userId: String): UserRefreshToken?
    fun findByUserIdAndRefreshToken(userId: String, refreshToken: String): UserRefreshToken?
}
