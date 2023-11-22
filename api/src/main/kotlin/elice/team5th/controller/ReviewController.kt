package elice.team5th.controller

import elice.team5th.domain.review.dto.ReviewDTO
import elice.team5th.domain.review.service.ReviewService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("api/v1/reviews")
class ReviewController(private val reviewService: ReviewService) {

    // 리뷰 작성
    @PostMapping
    fun createReview(@RequestBody reviewDTO: ReviewDTO): ResponseEntity<ReviewDTO> {
        val review = reviewService.createReview(reviewDTO)
        return ResponseEntity.ok(ReviewDTO(review.id, review.userId, review.contentId, review.detail, review.rating, review.likes, review.reports))
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    fun updateReview(@PathVariable id: Long, @RequestBody reviewDTO: ReviewDTO, principal: Principal): ResponseEntity<ReviewDTO> {
        val updatedReview = reviewService.updateReview(id, reviewDTO)
        return ResponseEntity.ok(ReviewDTO(updatedReview.id, updatedReview.userId, updatedReview.contentId, updatedReview.detail, updatedReview.rating, updatedReview.likes, updatedReview.reports))
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id: Long): ResponseEntity<Void> {
        reviewService.deleteReview(id)
        return ResponseEntity.ok().build()
    }

    // user_id로 페이징된 리뷰 리스트 조회
    @GetMapping("/user/{userId}")
    fun getReviewsByUserIdPaginated(
        @PathVariable userId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<ReviewDTO>> {
        val pageOfReviews = reviewService.findReviewsByUserIdPaginated(userId, page, size)
        val reviewDTOs = pageOfReviews.map { ReviewDTO(it.id, it.userId, it.contentId, it.detail, it.rating, it.likes, it.reports) }
        return ResponseEntity.ok(reviewDTOs)
    }

    // content_id로 페이징된 리뷰 리스트 조회
    @GetMapping("/content/{contentId}")
    fun getReviewsByContentIdPaginated(
        @PathVariable contentId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<ReviewDTO>> {
        val pageOfReviews = reviewService.findReviewsByContentIdPaginated(contentId, page, size)
        return ResponseEntity.ok(pageOfReviews.map { ReviewDTO(it.id, it.userId, it.contentId, it.detail, it.rating, it.likes, it.reports) })
    }

    // 최신순 리뷰 조회
    @GetMapping("/latest")
    fun getLatestReviews(): ResponseEntity<List<ReviewDTO>> {
        val reviews = reviewService.findAllReviewsOrderByCreatedAtDesc()
        val reviewDTOs = reviews.map { ReviewDTO(it.id, it.userId, it.contentId, it.detail, it.rating, it.likes, it.reports) }
        return ResponseEntity.ok(reviewDTOs)
    }

    // 추천순 리뷰 조회
    @GetMapping("/popular")
    fun getPopularReviews(): ResponseEntity<List<ReviewDTO>> {
        val reviews = reviewService.findAllReviewsOrderByLikesDesc()
        val reviewDTOs = reviews.map { ReviewDTO(it.id, it.userId, it.contentId, it.detail, it.rating, it.likes, it.reports) }
        return ResponseEntity.ok(reviewDTOs)
    }

    // 추천 기능
    @PutMapping("/{id}/like")
    fun likeReview(@PathVariable id: Long): ResponseEntity<Void> {
        reviewService.likeReview(id)
        return ResponseEntity.ok().build()
    }

    // 신고 기능
    @PutMapping("/{id}/report")
    fun reportReview(@PathVariable id: Long): ResponseEntity<Void> {
        reviewService.reportReview(id)
        return ResponseEntity.ok().build()
    }

}
