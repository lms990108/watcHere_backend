package elice.team5th.domain.clicks.entity

import elice.team5th.domain.tmdb.entity.TVShowEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tvshow_clicks")
class TVShowClicksEntity(
    @Id
    @Column(name = "tvshow_id")
    val tvShowId: Long,

    var clicks: Int,

    @OneToOne(optional = true) // tvshow 필드가 반드시 필요하지 않도록 optional = true 설정
    @JoinColumn(name = "tvshow_id", referencedColumnName = "id", insertable = false, updatable = false)
    val tvShow: TVShowEntity? = null // Nullable로 선언하여 tvshow를 반드시 가지고 있지 않아도 되게 함

)
