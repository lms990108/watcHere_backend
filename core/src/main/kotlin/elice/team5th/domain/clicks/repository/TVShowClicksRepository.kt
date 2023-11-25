package elice.team5th.domain.clicks.repository

import elice.team5th.domain.clicks.entity.TVShowClicksEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TVShowClicksRepository : JpaRepository<TVShowClicksEntity, Long> {
    @Query("SELECT t FROM TVShowClicksEntity t ORDER BY t.views DESC")
    fun findAllOrderByViewsDesc(): List<TVShowClicksEntity>
}
