package elice.team5th.domain.tmdb.repository

import elice.team5th.domain.tmdb.entity.TVShowEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TVShowRepository : JpaRepository<TVShowEntity, Int>
