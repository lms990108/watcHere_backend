package elice.team5th.domain.clicks.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tvshow_clicks")
class TVShowClicksEntity(
    @Id
    @Column(name = "tvshow_id")
    val tvShowId: Long,

    var views: Int
)
