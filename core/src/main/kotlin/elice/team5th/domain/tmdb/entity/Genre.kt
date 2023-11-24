package elice.team5th.domain.tmdb.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "genres")
class Genre(

    @Id
    val id: Long,

    val name: String,

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    val movies: List<MovieEntity> = mutableListOf(),

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    val tvShows: List<TVShowEntity> = mutableListOf()
)
