package elice.team5th.domain.user.model

import elice.team5th.common.model.BaseEntity
import elice.team5th.domain.chat.model.Message
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.jetbrains.annotations.NotNull

@Entity
class User(

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val messages: List<Message> = mutableListOf(),

    @NotNull
    val userId: String,
    var email: String?,
    var nickname: String,
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
