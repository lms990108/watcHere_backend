package elice.team5th.common.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
open class BaseChartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(nullable = false)
    var ranking: Int = 0 // 순위

    @Column(nullable = false, length = 20)
    var title: String = "" // 제목

    @Column(name = "poster_image", nullable = false, length = 255)
    var posterImage: String = "" // 포스터 이미지

    @Column(name = "link", nullable = false, length = 255)
    var link: String = "" // 링크

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null
}
