package elice.team5th.domain.tmdb.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "videos")
class VideoEntity(

    @Id
    val id: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = true)
    val movie: MovieEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tvshow_id", nullable = true)
    val tvShow: TVShowEntity? = null,

    @Column(name = "video_key")
    val key: String,

    val name: String,
    val site: String,
    val type: String,
)
