package elice.team5th.domain.chart.model

import elice.team5th.common.model.BaseChartEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "disneyplus_tvshow_chart")
class DisneyPlusTvShowChart : BaseChartEntity()
