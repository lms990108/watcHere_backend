package elice.team5th.domain.user.model

import elice.team5th.common.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    val userId: String = UUID.randomUUID().toString(),
    var profileImage: String? = null,
    var nickname: String,
    val socialProvider: ProviderType,
    val role: RoleType = RoleType.USER,
    var ban: Boolean = false

    //  val favorites : Array<Favorite>
    //  val reviews : Array<Review>
) : BaseEntity()
