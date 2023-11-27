package elice.team5th.domain.clicks.repository

import elice.team5th.domain.clicks.dto.MovieWithClicksDto
import elice.team5th.domain.clicks.entity.MovieClicksEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MovieClicksRepository : JpaRepository<MovieClicksEntity, Long> {
    @Query(
        "SELECT new elice.team5th.domain.clicks.dto.MovieWithClicksDto(m.id, m.title, m.posterPath, c.clicks) " +
            "FROM MovieClicksEntity c JOIN c.movie m " +
            "ORDER BY c.clicks DESC"
    )
    fun findAllMoviesWithClicks(pageable: Pageable): Page<MovieWithClicksDto>
}
