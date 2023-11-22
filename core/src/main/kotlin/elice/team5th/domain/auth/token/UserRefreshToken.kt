package elice.team5th.domain.auth.token

import elice.team5th.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user_refresh_tokens")
class UserRefreshToken(
    private val userId: String,
    var refreshToken: String
) : BaseEntity()
