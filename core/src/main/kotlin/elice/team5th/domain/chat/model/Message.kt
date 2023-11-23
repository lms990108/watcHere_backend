package elice.team5th.domain.chat.model

import elice.team5th.common.model.BaseEntity
import elice.team5th.domain.user.model.User
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "messages")
class Message(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    val user: User,

    @NotNull
    val text: String?,
    val type: MessageType

) : BaseEntity()
