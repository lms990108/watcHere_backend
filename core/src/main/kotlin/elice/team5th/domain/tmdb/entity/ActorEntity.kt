package elice.team5th.domain.tmdb.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "actors")
class ActorEntity {
    @Id
    val id: Long? = null
    val name: String? = null
    val profilePath: String? = null

    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY)
    val tvShows: List<TVShowEntity> = ArrayList()

    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY)
    val movies: List<MovieEntity> = ArrayList()
}
