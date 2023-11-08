package elice.team5th.elice.team5h.common.model

import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners
open class BaseTimeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    @CreatedDate
    open var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    open var updatedAt: LocalDateTime? = null
)
