package elice.team5th.common.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class BaseChartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
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
