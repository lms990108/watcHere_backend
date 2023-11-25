package elice.team5th.domain.clicks.repository

import elice.team5th.domain.clicks.dto.TVShowWithClicksDto
import elice.team5th.domain.clicks.entity.TVShowClicksEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TVShowClicksRepository : JpaRepository<TVShowClicksEntity, Long> {
    @Query("SELECT new elice.team5th.domain.clicks.dto.TVShowWithClicksDto(t.id, t.name, t.posterPath, c.clicks) " +
        "FROM TVShowClicksEntity c JOIN c.tvShow t " +
        "ORDER BY c.clicks DESC")
    fun findAllTVShowsWithClicks(pageable: Pageable): Page<TVShowWithClicksDto>
}
