package elice.team5th.domain.user.model

import elice.team5th.common.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "users")
class User(
    @NotNull
    @Column(name = "user_id", unique = true, length = 64)
    val userId: String,

    var email: String?,

    var nickname: String,

    @Column(name = "profile_image", length = 512)
    var profileImage: String,

    @Enumerated(EnumType.STRING)
    @NotNull
    val provider: ProviderType,

    @Enumerated(EnumType.STRING)
    @NotNull
    var role: RoleType = RoleType.USER,

    @NotNull
    var ban: Boolean = false

    //  val favorites : Array<Favorite>
    //  val reviews : Array<Review>
) : BaseEntity()
