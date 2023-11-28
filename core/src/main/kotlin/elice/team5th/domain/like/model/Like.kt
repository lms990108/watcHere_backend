package elice.team5th.domain.like.model

import elice.team5th.common.model.BaseEntity
import elice.team5th.domain.tmdb.entity.MovieEntity
import elice.team5th.domain.tmdb.entity.TVShowEntity
import elice.team5th.domain.user.model.User
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "likes")
class Like(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
    var movie: MovieEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_show_id", referencedColumnName = "id", nullable = false)
    val tvShow: TVShowEntity? = null
) : BaseEntity()
