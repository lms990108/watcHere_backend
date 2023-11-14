package elice.team5th.domain.content.model
import jakarta.persistence.*

@Entity
@Table(name = "content")
class Content(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var type: String, // 예를 들어 "MOVIE", "DRAMA", "ENTERTAINMENT" 등의 값을 가질 수 있음

    @Column(nullable = true)
    var posterImage: String? = null,

    @Column(nullable = false)
    var starRating: Float,

    @Column(nullable = true)
    var director: String? = null,
)
