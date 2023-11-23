package elice.team5th.domain.review.service

import elice.team5th.domain.review.dto.CreateReviewDTO
import elice.team5th.domain.review.exception.PermissionDeniedException
import elice.team5th.domain.review.exception.ReviewNotFoundException
import elice.team5th.domain.review.exception.UnauthorizedReviewAccessException
import elice.team5th.domain.review.model.Review
import elice.team5th.domain.review.repository.ReviewRepository
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    fun createReview(createReviewDTO: CreateReviewDTO, user: User): Review {
        val review = Review(
            contentId = createReviewDTO.contentId,
            userId = user.userId.toLong(),
            detail = createReviewDTO.detail,
            rating = createReviewDTO.rating,
            likes = 0,
            reports = 0
        )
        return reviewRepository.save(review)
    }

    // 리뷰 수정
    @Transactional
    fun updateReview(id: Long, createReviewDTO: CreateReviewDTO): Review {
        // 현재 인증된 사용자의 정보를 SecurityContext에서 가져옵니다.
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = authentication.principal as UserDetails // UserDetails 사용은 스프링 시큐리티 설정에 따라 달라질 수 있습니다.
        val currentUserId = currentUser.username.toLong() // 사용자의 고유 ID를 가져옵니다.

        // 리뷰를 조회합니다.
        val review = reviewRepository.findById(id).orElseThrow {
            throw RuntimeException("Review not found")
        }

        // 현재 인증된 사용자가 리뷰 작성자와 동일한지 확인합니다.
        if (review.userId != currentUserId) {
            throw RuntimeException("Not authorized to update this review")
        }

        // 리뷰의 detail 필드만 수정합니다.
        review.apply {
            detail = createReviewDTO.detail
        }

        // 수정된 리뷰를 저장합니다.
        return reviewRepository.save(review)
    }

    @Transactional
    fun updateReview(id: Long, createReviewDTO: CreateReviewDTO, user: User): Review {
        val review = reviewRepository.findById(id).orElseThrow {
            ReviewNotFoundException("Review not found with ID: $id")
        }
        if (review.userId != user.id) { // user.id 또는 user.userId로 변경해야 할 수 있습니다.
            throw UnauthorizedReviewAccessException("User is not authorized to update review with ID: $id")
        }
        review.apply {
            detail = createReviewDTO.detail
        }
        return reviewRepository.save(review)
    }


    @Transactional
    fun deleteReview(id: Long, user: User) {
        val review = reviewRepository.findById(id).orElseThrow {
            ReviewNotFoundException("Review not found with ID: $id")
        }
        if (review.userId != user.id && user.role != RoleType.ADMIN) { // RoleType.ADMIN은 사용자 정의 enum에 따라 다를 수 있습니다.
            throw PermissionDeniedException("User does not have permission to delete review with ID: $id")
        }
        reviewRepository.deleteById(id)
    }

    // user_id로 페이징된 리뷰 리스트 조회
    fun findReviewsByUserIdPaginated(userId: Long, page: Int, size: Int): Page<Review> {
        val pageable: Pageable = PageRequest.of(page, size)
        return reviewRepository.findByUserId(userId, pageable)
    }

    // content_id로 페이징된 리뷰 리스트 조회
    // 컨텐츠 평균 평점 조회 가능하도록 수정
    // 컨텐츠 총 리뷰 갯수 조회 가능하도록 수정
    fun findReviewsByContentIdPaginated(contentId: Long, page: Int, size: Int): Page<Review> {
        val pageable: Pageable = PageRequest.of(page, size)
        return reviewRepository.findByContentId(contentId, pageable)
    }

    // 해당하는 리뷰 갯수만

    // 최신순 정렬 조회
    fun findAllReviewsOrderByCreatedAtDesc(): List<Review> {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
    }

    // 추천순 정렬 조회
    fun findAllReviewsOrderByLikesDesc(): List<Review> {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "likes"))
    }

    // 별점높은순 & 낮은순

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
}
