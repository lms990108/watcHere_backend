package elice.team5th.domain.review.repository

import elice.team5th.domain.review.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<Review>
    fun findByContentId(contentId: Long, pageable: Pageable): Page<Review>
}
