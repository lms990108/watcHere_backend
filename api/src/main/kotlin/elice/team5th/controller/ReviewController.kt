package elice.team5th.controller

import elice.team5th.domain.auth.annotation.CurrentUser
import elice.team5th.domain.auth.entity.UserPrincipal
import elice.team5th.domain.review.dto.CreateReviewDTO
import elice.team5th.domain.review.dto.RatingCountDTO
import elice.team5th.domain.review.dto.ReviewDTO
import elice.team5th.domain.review.dto.ReviewPageDataDTO
import elice.team5th.domain.review.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    @Operation(
        summary = "리뷰 작성",
        description = "userId는 자동으로 가져오니 contentId, 내용, 평점만 적으면 됩니다."
    )
    fun createReview(@RequestBody createReviewDTO: CreateReviewDTO, @CurrentUser user: UserPrincipal):
        ResponseEntity<ReviewDTO> {
        val review = reviewService.createReview(createReviewDTO, user)
        println(createReviewDTO)
        return ResponseEntity.ok().body(ReviewDTO(review))
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    @Operation(
        summary = "리뷰 수정",
        description = "백엔드로 보낼땐 안바뀐 부분도 리뷰작성과 똑같이 보내주세요."
    )
    fun updateReview(
        @PathVariable id: Long,
        @RequestBody createReviewDTO: CreateReviewDTO,
        @CurrentUser user: UserPrincipal
    ): ResponseEntity<ReviewDTO> {
        val updatedReview = reviewService.updateReview(id, createReviewDTO, user)
        return ResponseEntity.ok().body(ReviewDTO(updatedReview))
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    @Operation(
        summary = "리뷰 삭제",
        description = "작성자와 관리자만 삭제 가능합니다."
    )
    fun deleteReview(@PathVariable id: Long, @CurrentUser user: UserPrincipal): ResponseEntity<Void> {
        reviewService.deleteReview(id, user)
        return ResponseEntity.ok().build()
    }

    // user_id로 페이징된 리뷰 리스트 조회
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "해당 유저의 리뷰 목록 조회",
        description = "10개씩 페이징되고 추가적인 정렬방식 등은 넣지 않았습니다."
    )
    fun getReviewsByUserIdPaginated(
        @PathVariable userId: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<ReviewDTO>> {
        val pageOfReviews = reviewService.findReviewsByUserIdPaginated(userId, page, size)
        val reviewDTOs = pageOfReviews.map { ReviewDTO(it) }
        return ResponseEntity.ok(reviewDTOs)
    }

    // content_id로 페이징된 리뷰 리스트 조회
    @GetMapping("/content/{contentId}")
    @Operation(
        summary = "컨텐츠의 리뷰 목록 조회",
        description = "정렬기능을 지원합니다 sortBy = \n" +
            "ratingDesc: 평점 높은순 \n" +
            "ratingAsc: 평점 낮은순 \n" +
            "likesDesc: 추천순\n" +
            "createdAt: 기본값, 최신순"
    )
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
    @Operation(summary = "특정 별점의 리뷰 갯수를 반환합니다")
    fun getReviewRatingsCount(@PathVariable contentId: Long): ResponseEntity<RatingCountDTO> {
        val ratingCounts = reviewService.getRatingCountsForContentId(contentId)
        return ResponseEntity.ok(ratingCounts)
    }

    // 추천 기능
    @PutMapping("/{id}/like")
    @Operation(summary = "추천 버튼")
    fun likeReview(@PathVariable id: Long): ResponseEntity<Void> {
        reviewService.likeReview(id)
        return ResponseEntity.ok().build()
    }

    // 신고 기능
    // 인증된 유저만 신고할 수 있게끔 수정해야함
    @PutMapping("/{id}/report")
    @Operation(summary = "신고 버튼")
    fun reportReview(@PathVariable id: Long): ResponseEntity<Void> {
        reviewService.reportReview(id)
        return ResponseEntity.ok().build()
    }

    // report >= 5 인 리뷰들 리스트 조회
    @GetMapping("/reviews/high-reports")
    @Operation(
        summary = "높은 신고 수를 받은 리뷰 목록 조회",
        description = "페이지당 10개의 리뷰를 페이징하여 제공합니다. 높은 신고 수를 받은 리뷰들을 조회할 수 있습니다."
    )
    fun getReviewsWithHighReports(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<ReviewDTO>> {
        val pageable: Pageable = PageRequest.of(page, size)
        val pageOfReviews = reviewService.findReviewsWithHighReports(pageable)
        val reviewDTOs = pageOfReviews.map { ReviewDTO(it) }
        return ResponseEntity.ok(reviewDTOs)
    }
}
