package elice.team5th.controller

import elice.team5th.domain.auth.annotation.CurrentUser
import elice.team5th.domain.review.dto.CreateReviewDTO
import elice.team5th.domain.review.dto.RatingCountDTO
import elice.team5th.domain.review.dto.ReviewDTO
import elice.team5th.domain.review.dto.ReviewPageDataDTO
import elice.team5th.domain.review.service.ReviewService
import elice.team5th.domain.user.model.User
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/reviews")
class ReviewController(private val reviewService: ReviewService) {

    // 리뷰 작성
    @PostMapping("")
    fun createReview(@RequestBody createReviewDTO: CreateReviewDTO, @CurrentUser user: User):
        ResponseEntity<ReviewDTO> {
        val review = reviewService.createReview(createReviewDTO, user)
        return ResponseEntity.ok(
            ReviewDTO(
                review.id,
                review.userId,
                review.contentId,
                review.detail,
                review.rating,
                review.likes,
                review.reports
            )
        )
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    fun updateReview(
        @PathVariable id: Long,
        @RequestBody createReviewDTO: CreateReviewDTO,
        @CurrentUser user: User
    ): ResponseEntity<ReviewDTO> {
        val updatedReview = reviewService.updateReview(id, createReviewDTO, user)
        return ResponseEntity.ok(
            ReviewDTO(
                updatedReview.id,
                updatedReview.userId,
                updatedReview.contentId,
                updatedReview.detail,
                updatedReview.rating,
                updatedReview.likes,
                updatedReview.reports
            )
        )
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id: Long, @CurrentUser user: User): ResponseEntity<Void> {
        reviewService.deleteReview(id, user)
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
        val reviewDTOs = pageOfReviews.map {
            ReviewDTO(
                it.id,
                it.userId,
                it.contentId,
                it.detail,
                it.rating,
                it.likes,
                it.reports
            )
        }
        return ResponseEntity.ok(reviewDTOs)
    }

    // content_id로 페이징된 리뷰 리스트 조회
    @GetMapping("/content/{contentId}")
    fun getReviewsByContentIdPaginated(
        @PathVariable contentId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAtDesc") sortBy: String // 정렬 기준 추가
    ): ResponseEntity<ReviewPageDataDTO> {
        val reviewPageData = reviewService.findReviewsByContentIdPaginated(contentId, page, size, sortBy)
        return ResponseEntity.ok(reviewPageData)
    }

    // 리뷰 별점당 갯수 조회
    @GetMapping("/ratings/{contentId}")
    fun getReviewRatingsCount(@PathVariable contentId: Long): ResponseEntity<RatingCountDTO> {
        val ratingCounts = reviewService.getRatingCountsForContentId(contentId)
        return ResponseEntity.ok(ratingCounts)
    }

    // 추천 기능
    @PutMapping("/{id}/like")
    fun likeReview(@PathVariable id: Long): ResponseEntity<Void> {
        reviewService.likeReview(id)
        return ResponseEntity.ok().build()
    }

    // 신고 기능
    // 인증된 유저만 신고할 수 있게끔 수정해야함
    @PutMapping("/{id}/report")
    fun reportReview(@PathVariable id: Long): ResponseEntity<Void> {
        reviewService.reportReview(id)
        return ResponseEntity.ok().build()
    }

    // report >= 5 인 리뷰들 리스트 조회
    @GetMapping("/high-reports")
    fun getReviewsWithHighReports(): ResponseEntity<List<ReviewDTO>> {
        val reviews = reviewService.findReviewsWithHighReports()
        val reviewDTOs = reviews.map { review ->
            ReviewDTO(
                id = review.id,
                userId = review.userId,
                contentId = review.contentId,
                detail = review.detail,
                rating = review.rating,
                likes = review.likes,
                reports = review.reports
            )
        }
        return ResponseEntity.ok(reviewDTOs)
    }
}
