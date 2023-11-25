package elice.team5th.domain.clicks.repository

import elice.team5th.domain.clicks.entity.MovieClicksEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MovieClicksRepository : JpaRepository<MovieClicksEntity, Long> {
    @Query("SELECT t FROM MovieClicksEntity t ORDER BY t.views DESC")
    fun findAllOrderByViewsDesc(): List<MovieClicksEntity>
}
