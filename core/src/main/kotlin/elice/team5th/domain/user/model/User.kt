package elice.team5th.domain.user.model

import elice.team5th.common.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @NotNull
    @Column(name = "user_id", unique = true, length = 64)
    val userId: String,

    var email: String?,

    var nickname: String,

    @Column(name = "profile_image", length = 512)
    var profileImage: String? = null,

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "provider_type", length = 20)
    val providerType: ProviderType,

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role_type", length = 20)
    val role: RoleType = RoleType.USER,

    @NotNull
    var ban: Boolean = false

    //  val favorites : Array<Favorite>
    //  val reviews : Array<Review>
) : BaseEntity()
