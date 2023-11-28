package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.VideoEntity

data class VideoDto(
    val key: String,
    val name: String,
    val site: String,
    val type: String
) {
    constructor(video: VideoEntity) : this(
        key = video.key,
        name = video.name,
        site = video.site,
        type = video.type
    )
}
