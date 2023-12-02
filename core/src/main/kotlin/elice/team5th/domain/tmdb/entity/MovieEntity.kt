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
@Table(name = "movies")
class MovieEntity(

    @Id
    val id: Long,

    val adult: Boolean,

    @Column(name = "backdrop_path")
    val backdropPath: String,

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "movie_genre",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: List<GenreEntity> = mutableListOf(),

    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL])
    val videos: List<VideoEntity> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "movie_actor",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "actor_id")]
    )
    val actors: MutableList<ActorEntity> = mutableListOf(),

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    @Column(name = "likes")
    val likes: MutableList<Like> = mutableListOf(),

    @Column(name = "imdb_id")
    val imdbId: String,

    @Column(name = "original_language")
    val originalLanguage: String,

    @Column(name = "original_title")
    val originalTitle: String,

    @Column(columnDefinition = "TEXT")
    val overview: String,

    val popularity: Double,

    @Column(name = "poster_path")
    val posterPath: String,

    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    val releaseDate: Date,

    val revenue: Long,

    val runtime: Int,

    val title: String,

    val video: Boolean,

    @Column(name = "vote_average")
    val voteAverage: Double,

    @Column(name = "vote_count")
    val voteCount: Int,

    @Column(name = "director_name")
    val directorName: String? = null,

    @Column(name = "director_profile_path")
    val directorProfilePath: String? = null,

    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
