package elice.team5th.domain.review.service

import elice.team5th.domain.review.dto.CreateReviewDTO
import elice.team5th.domain.review.model.Review
import elice.team5th.domain.review.repository.ReviewRepository
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

    fun createReview(createReviewDTO: CreateReviewDTO, user: UserDetails): Review {
        val review = Review(
            contentId = createReviewDTO.contentId,
            userId = user.username.toLong(),
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
    fun updateReview(id: Long, createReviewDTO: CreateReviewDTO, user: UserDetails): Review {
        val review = reviewRepository.findById(id).orElseThrow {
            throw RuntimeException("Review not found")
        }
        if (review.userId != user.username.toLong()) {
            throw RuntimeException("Not authorized to update this review")
        }
        review.apply {
            detail = createReviewDTO.detail
        }
        return reviewRepository.save(review)
    }

    @Transactional
    fun deleteReview(id: Long, user: UserDetails) {
        val review = reviewRepository.findById(id).orElseThrow {
            throw RuntimeException("Review not found")
        }
        val currentUserId = user.username.toLong()
        val hasRoleAdmin = user.authorities.any { it.authority == "ROLE_ADMIN" }
        if (review.userId != currentUserId && !hasRoleAdmin) {
            throw RuntimeException("You do not have permission to delete this review")
        }
        reviewRepository.deleteById(id)
    }

    // user_id로 페이징된 리뷰 리스트 조회
    fun findReviewsByUserIdPaginated(userId: Long, page: Int, size: Int): Page<Review> {
        val pageable: Pageable = PageRequest.of(page, size)
        return reviewRepository.findByUserId(userId, pageable)
    }

    // content_id로 페이징된 리뷰 리스트 조회
    fun findReviewsByContentIdPaginated(contentId: Long, page: Int, size: Int): Page<Review> {
        val pageable: Pageable = PageRequest.of(page, size)
        return reviewRepository.findByContentId(contentId, pageable)
    }

    // 최신순 정렬 조회
    fun findAllReviewsOrderByCreatedAtDesc(): List<Review> {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
    }

    // 추천순 정렬 조회
    fun findAllReviewsOrderByLikesDesc(): List<Review> {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "likes"))
    }

    // 좋아요(추천) 기능
    @Transactional
    fun likeReview(id: Long): Review {
        val review = reviewRepository.findById(id).orElseThrow {
            throw RuntimeException("Review not found")
        }
        review.likes += 1
        return reviewRepository.save(review)
    }

    // 신고 기능
    @Transactional
    fun reportReview(id: Long): Review {
        val review = reviewRepository.findById(id).orElseThrow {
            throw RuntimeException("Review not found")
        }
        review.reports += 1
        return reviewRepository.save(review)
    }

}
