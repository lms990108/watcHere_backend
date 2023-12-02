package elice.team5th.domain.review.repository

import elice.team5th.domain.review.model.Review
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByUser(user: User, pageable: Pageable): Page<Review>
    fun findByContentId(contentId: Long, pageable: Pageable): Page<Review>

    fun findByContentIdAndContentType(contentId: Long, contentType: ContentType, pageable: Pageable): Page<Review> // 타입까지 고려

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.contentId = :contentId AND r.contentType = :contentType")
    fun findAverageRatingByContentIdAndContentType(
        @Param("contentId") contentId: Long?,
        @Param("contentType") contentType: ContentType?
    ): Double?
    fun findByDetailContaining(detail: String, pageable: Pageable): Page<Review>

    fun findByUserUserIdAndContentId(userId: String, contentId: Long): Review?
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.contentId = :contentId")
    fun findAverageRatingByContentId(@Param("contentId") contentId: Long): Double?

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.contentId = :contentId GROUP BY r.rating")
    fun countReviewsByRatingForContentId(@Param("contentId") contentId: Long): List<Array<Any>>

    fun findByReportsGreaterThanEqual(reportsThreshold: Int, pageable: Pageable): Page<Review>
    fun findByUserUserIdAndContentIdAndContentType(userId: String, contentId: Int, contentType: ContentType): Review?

}
