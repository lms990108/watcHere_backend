package elice.team5th.domain.tmdb.entity

import elice.team5th.common.model.BaseEntity
import elice.team5th.domain.user.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Content (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    var userByFavorite: User? = null,

    @Id
    open var id: Long? = null,

    @CreatedDate
    open var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    open var updatedAt: LocalDateTime = LocalDateTime.now(),

    open var adult: Boolean = false,

    @Column(name = "backdrop_path")
    open val backdropPath: String = "",

    @Column(columnDefinition = "TEXT")
    open val overview: String = "",

    open val popularity: Double = 0.0,

    @Column(name = "poster_path")
    open val posterPath: String = "",

    @Column(name = "vote_average")
    open val voteAverage: Double = 0.0,

    @Column(name = "vote_count")
    open val voteCount: Int = 0,

    // 추가된 필드
    @Column(name = "director_name")
    open val directorName: String? = null,

    @Column(name = "director_profile_path")
    open val directorProfilePath: String? = null
)
