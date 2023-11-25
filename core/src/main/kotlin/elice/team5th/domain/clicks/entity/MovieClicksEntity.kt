package elice.team5th.domain.clicks.entity

import elice.team5th.domain.tmdb.entity.MovieEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "movie_clicks")
class MovieClicksEntity(
    @Id
    @Column(name = "movie_id")
    val movieId: Long,

    var clicks: Int = 0,

    @OneToOne(optional = true) // movie 필드가 반드시 필요하지 않도록 optional = true 설정
    @JoinColumn(name = "movie_id", referencedColumnName = "id", insertable = false, updatable = false)
    val movie: MovieEntity? = null // Nullable로 선언하여 movie를 반드시 가지고 있지 않아도 되게 함
)
