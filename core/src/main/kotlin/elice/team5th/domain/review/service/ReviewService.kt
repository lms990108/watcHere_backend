package elice.team5th.domain.review.service

import elice.team5th.domain.review.dto.ReviewDTO
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

    @Transactional
    fun createReview(reviewDTO: ReviewDTO): Review {
        // 현재 인증된 사용자의 정보를 SecurityContext에서 가져옵니다.
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication.principal as UserDetails // 여기서 UserDetails를 사용하는 것은 스프링 시큐리티 설정에 따라 달라질 수 있습니다.

        val review = Review(
            contentId = reviewDTO.contentId,
            userId = user.username.toLong(), // UserDetails에서 가져온 사용자 ID
            detail = reviewDTO.detail,
            rating = reviewDTO.rating,
            likes = 0, // 생성 시점에는 0으로 시작
            reports = 0 // 생성 시점에는 0으로 시작
        )
        return reviewRepository.save(review)
    }

    // 리뷰 수정
    @Transactional
    fun updateReview(id: Long, reviewDTO: ReviewDTO): Review {
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
            detail = reviewDTO.detail
        }

        // 수정된 리뷰를 저장합니다.
        return reviewRepository.save(review)
    }

    @Transactional
    fun deleteReview(id: Long) {
        // 현재 인증된 사용자의 정보를 SecurityContext에서 가져옵니다.
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = authentication.principal as UserDetails // UserDetails 사용은 스프링 시큐리티 설정에 따라 달라질 수 있습니다.
        val currentUserId = currentUser.username.toLong() // 사용자의 고유 ID를 가져옵니다.

        // 리뷰를 조회합니다.
        val review = reviewRepository.findById(id).orElseThrow {
            throw RuntimeException("Review not found")
        }

        // 현재 인증된 사용자가 리뷰 작성자와 동일하거나 관리자인지 확인합니다.
        val hasRoleAdmin = authentication.authorities.any { it.authority == "ROLE_ADMIN" }
        if (review.userId != currentUserId && !hasRoleAdmin) {
            throw RuntimeException("You do not have permission to delete this review")
        }

        // 리뷰를 삭제합니다.
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
