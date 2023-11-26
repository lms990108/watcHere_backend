package elice.team5th.domain.tmdb.repository

import elice.team5th.domain.tmdb.entity.MovieEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<MovieEntity, Int>
