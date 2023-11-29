package elice.team5th.domain.review.service

import elice.team5th.domain.auth.entity.UserPrincipal
import elice.team5th.domain.review.dto.CreateReviewDTO
import elice.team5th.domain.review.dto.RatingCountDTO
import elice.team5th.domain.review.dto.ReviewDTO
import elice.team5th.domain.review.dto.ReviewPageDataDTO
import elice.team5th.domain.review.exception.PermissionDeniedException
import elice.team5th.domain.review.exception.ReviewNotFoundException
import elice.team5th.domain.review.exception.UnauthorizedReviewAccessException
import elice.team5th.domain.review.model.Review
import elice.team5th.domain.review.repository.ReviewRepository
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val userService: UserService
) {

    // 리뷰 작성
    fun createReview(createReviewDTO: CreateReviewDTO, user: UserPrincipal): Review {
        val currentUser = userService.findUserById(user.userId)

        reviewRepository.findByUserUserIdAndContentIdAndContentType(currentUser.userId, createReviewDTO.contentId, createReviewDTO.contentType)?.let {
            throw Exception("이미 해당 컨텐츠(ID: ${createReviewDTO.contentId}, Type: ${createReviewDTO.contentType})에 대한 리뷰가 존재합니다.")
        }

        val review = Review(
            user = currentUser,
            contentId = createReviewDTO.contentId,
            contentType = createReviewDTO.contentType,
            detail = createReviewDTO.detail,
            rating = createReviewDTO.rating,
            likes = 0,
            reports = 0
        )
        println(createReviewDTO.contentId)
        return reviewRepository.save(review)
    }


    // 리뷰 수정
    @Transactional
    fun updateReview(id: Long, createReviewDTO: CreateReviewDTO, user: UserPrincipal): Review {
        val review = reviewRepository.findById(id).orElseThrow {
            ReviewNotFoundException("Review not found with ID: $id")
        }
        if (review.user.userId != user.userId) {
            throw UnauthorizedReviewAccessException("User is not authorized to update review with ID: $id")
        }

        review.apply {
            detail = createReviewDTO.detail
            rating = createReviewDTO.rating
        }
        return reviewRepository.save(review)
    }

    // 리뷰 삭제
    @Transactional
    fun deleteReview(id: Long, user: UserPrincipal) {
        val review = reviewRepository.findById(id).orElseThrow {
            ReviewNotFoundException("Review not found with ID: $id")
        }
        if (review.user.userId != user.userId && user.roleType != RoleType.ADMIN) { // RoleType.ADMIN은 사용자 정의 enum에 따라 다를 수 있습니다.
            throw PermissionDeniedException("User does not have permission to delete review with ID: $id")
        }
        reviewRepository.deleteById(id)
    }

    // user_id로 페이징된 리뷰 리스트 조회
    fun findReviewsByUserIdPaginated(userId: String, page: Int, size: Int): Page<Review> {
        val pageable: Pageable = PageRequest.of(page, size)
        val user = userService.findUserById(userId)
        return reviewRepository.findByUser(user, pageable)
    }

    // content_id로 페이징된 리뷰 리스트 조회
    // 컨텐츠 평균 평점 조회 가능하도록 수정
    // 컨텐츠 총 리뷰 갯수 조회 가능하도록 수정
    fun findReviewsByContentIdPaginated(
        contentId: Long,
        contentType: ContentType,
        page: Int,
        size: Int,
        sortBy: String = "createdAtDesc"
    ): ReviewPageDataDTO {
        val sort = when (sortBy) {
            "ratingDesc" -> Sort.by(Sort.Direction.DESC, "rating")
            "ratingAsc" -> Sort.by(Sort.Direction.ASC, "rating")
            "likesDesc" -> Sort.by(Sort.Direction.DESC, "likes")
            else -> Sort.by(Sort.Direction.DESC, "createdAt") // 기본값은 최신순
        }
        val pageable = PageRequest.of(page, size, sort)

        val pageOfReviews = reviewRepository.findByContentIdAndContentType(contentId, contentType, pageable)
        val averageRating = reviewRepository.findAverageRatingByContentIdAndContentType(contentId, contentType) ?: 0.0

        return ReviewPageDataDTO(
            reviews = pageOfReviews.map { ReviewDTO(it) },
            averageRating = averageRating,
            totalElements = pageOfReviews.totalElements
        )
    }

    // 특정 별점에 해당하는 리뷰 갯수
    fun getRatingCountsForContentId(contentId: Long): RatingCountDTO {
        val ratingCounts = reviewRepository.countReviewsByRatingForContentId(contentId)
        val ratingMap = ratingCounts.associate { (rating, count) ->
            rating as Int to (count as Long)
        }
        return RatingCountDTO(ratingMap)
    }

    // 좋아요(추천) 기능
    @Transactional
    fun likeReview(id: Long): Review {
        val review = reviewRepository.findById(id).orElseThrow {
            ReviewNotFoundException("Review not found with ID: $id")
        }
        review.likes += 1
        return reviewRepository.save(review)
    }

    // 신고 기능
    @Transactional
    fun reportReview(id: Long): Review {
        val review = reviewRepository.findById(id).orElseThrow {
            ReviewNotFoundException("Review not found with ID: $id")
        }
        review.reports += 1
        return reviewRepository.save(review)
    }

    // 누적신고 5회 이상 리뷰 리스트 조회
    // 서비스 클래스 내
    fun findReviewsWithHighReports(pageable: Pageable): Page<Review> {
        return reviewRepository.findByReportsGreaterThanEqual(5, pageable)
    }

    // 특정 컨텐츠에서 내 리뷰 찾기
    fun findMyReviewByContentId(contentId: Long, contentType: ContentType, user: UserPrincipal): Review {
        return reviewRepository.findByUserUserIdAndContentIdAndContentType(user.userId, contentId.toInt(), contentType)
            ?: throw ReviewNotFoundException("Review not found with contentId: $contentId and contentType: $contentType")
    }

    fun findAllReviewsPaginated(page: Int, size: Int): Page<Review> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending())
        return reviewRepository.findAll(pageable)
    }
}
