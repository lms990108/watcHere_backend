package elice.team5th.domain.content.model

import elice.team5th.elice.team5h.common.model.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "content")
class Content : BaseTimeEntity() {
    @Column(nullable = false, length = 20)
    var title: String = "" // non-nullable로 변경

    @Column(nullable = false)
    var type: Int = 0 // 1: MOVIE, 2: DRAMA, 3: ENTERTAINMENT, 4: ANIMATION

    @Column(name = "poster_image", length = 30, nullable = false)
    var posterImage: String = "" // non-nullable로 변경

    @Column(name = "star_rating", nullable = false)
    var starRating: Int? = null

    @Column(nullable = false, length = 30)
    var director: String = "" // non-nullable로 변경

    @Column(nullable = true, length = 10)
    var genre: String? = null

    @Column(nullable = true)
    var release: LocalDateTime? = null

    @Column(name = "episode_date", nullable = true)
    var episodeDate: String? = null

    @Column(nullable = true)
    var season: Int? = null
}
