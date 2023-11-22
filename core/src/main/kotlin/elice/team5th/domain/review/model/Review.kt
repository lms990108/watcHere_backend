package elice.team5th.domain.review.model

import elice.team5th.common.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "review")
class Review(

    @Column(name = "content_id", nullable = false)
    var contentId: Long = 0, // contentID에 대한 기본값 지정

    @Column(name = "user_id", nullable = false)
    var userId: Long = 0, // userID에 대한 기본값 지정

    @Column(nullable = false)
    var detail: String = "", // 기본값을 제공하거나 생성자를 통해 값을 받아야 합니다.

    @Column(nullable = false)
    var rating: Double = 0.0, // rating에 대한 기본값 지정

    @Column(nullable = false)
    var likes: Int = 0, // 'likes'는 추천 수를 나타냄

    @Column(nullable = false)
    var reports: Int = 0 // 'reports'는 신고 수를 나타냄


) : BaseEntity() // BaseEntity의 생성자 호출
