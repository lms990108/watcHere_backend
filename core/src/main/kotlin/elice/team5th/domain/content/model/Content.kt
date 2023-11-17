package elice.team5th.domain.content.model

import elice.team5th.elice.team5h.common.model.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "content")
<<<<<<< HEAD
class Content : BaseTimeEntity() {
    @Column(nullable = false, length = 20)
    var title: String = "" // 제목
=======
class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
>>>>>>> 5444b12 (Feat: 소셜 로그인 draft)

    @Column(nullable = false)
    var type: Int = 0 // 콘텐츠 타입 (1: MOVIE, 2: DRAMA, etc.)

    @Column(name = "poster_image", length = 30, nullable = false)
    var posterImage: String = "" // 포스터 이미지

    @Column(name = "star_rating", nullable = false)
    var starRating: Float? = null // 별점

    @Column(nullable = false, length = 30)
    var director: String = "" // 감독

<<<<<<< HEAD
    @Column(nullable = false, length = 100)
    var provider: String = "" // 컨텐츠 제공 플랫폼
}
=======
    @Column(nullable = true)
    var director: String? = null
)
>>>>>>> 5444b12 (Feat: 소셜 로그인 draft)
