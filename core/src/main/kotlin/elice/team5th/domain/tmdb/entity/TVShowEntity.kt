package elice.team5th.domain.tmdb.entity

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
import java.util.*


@Entity
@Table(name = "tvshows")
class TVShowEntity(

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

    @Temporal(TemporalType.DATE)
    @Column(name = "first_air_date")
    var firstAirDate: Date? = null,

    @Temporal(TemporalType.DATE)
    @Column(name = "last_air_date")
    var lastAirDate: Date? = null,

    val name: String,

    val numberOfEpisodes: Int,

    val numberOfSeasons: Int,

    val type: String,
) : Content()
