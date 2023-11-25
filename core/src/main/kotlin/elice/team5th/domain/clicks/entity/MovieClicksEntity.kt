package elice.team5th.domain.clicks.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "movie_clicks")
class MovieClicksEntity(
    @Id
    @Column(name = "movie_id")
    val movieId: Long,

    var views: Int
)
