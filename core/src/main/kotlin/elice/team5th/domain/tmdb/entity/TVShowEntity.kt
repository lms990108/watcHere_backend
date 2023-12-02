package elice.team5th.domain.tmdb.entity

import elice.team5th.domain.like.model.Like
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tvshows")
class TVShowEntity(

    @Id
    val id: Long,

    val adult: Boolean,

    @Column(name = "backdrop_path")
    val backdropPath: String,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "tvshow_genre",
        joinColumns = [JoinColumn(name = "tvshow_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: List<GenreEntity> = mutableListOf(),

    @OneToMany(mappedBy = "tvShow", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val videos: List<VideoEntity> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "tvshow_actor",
        joinColumns = [JoinColumn(name = "tvshow_id")],
        inverseJoinColumns = [JoinColumn(name = "actor_id")]
    )
    val actors: MutableList<ActorEntity> = mutableListOf(),

    @OneToMany(mappedBy = "tvShow", fetch = FetchType.LAZY)
    @Column(name = "likes")
    val likes: MutableList<Like> = mutableListOf(),

    @Temporal(TemporalType.DATE)
    @Column(name = "first_air_date")
    var firstAirDate: Date? = null,

    @Temporal(TemporalType.DATE)
    @Column(name = "last_air_date")
    var lastAirDate: Date? = null,

    val name: String,

    val numberOfEpisodes: Int,

    val numberOfSeasons: Int,

    @Column(columnDefinition = "TEXT")
    val overview: String,

    val popularity: Double,

    @Column(name = "poster_path")
    val posterPath: String,

    val type: String,

    @Column(name = "vote_average")
    val voteAverage: Double,

    @Column(name = "vote_count")
    val voteCount: Int,

    // 추가된 필드
    @Column(name = "director_name")
    val directorName: String? = null,

    @Column(name = "director_profile_path")
    val directorProfilePath: String? = null,

    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
