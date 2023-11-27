package elice.team5th.domain.user.model

import elice.team5th.common.model.BaseEntity
import elice.team5th.domain.chat.model.Message
import elice.team5th.domain.review.model.Review
import elice.team5th.domain.tmdb.entity.Content
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val messages: List<Message> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val reviews : MutableList<Review> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val favorites : MutableList<Content> = mutableListOf(),

    @NotNull
    @Id
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
    var ban: Boolean = false,

    var deletedAt: LocalDateTime? = null,

    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
