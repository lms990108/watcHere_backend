package elice.team5th.domain.movie.model

import elice.team5th.domain.content.model.Content
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class Movie : Content() {
    @Column(nullable = true)
    var releaseDate: LocalDateTime? = null // 개봉일

    @Column(nullable = true)
    var genre: String? = null // 장르

    @Column(nullable = true)
    var summary: String? = null
}
