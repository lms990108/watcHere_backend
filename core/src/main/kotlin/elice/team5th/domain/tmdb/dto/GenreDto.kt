package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.GenreEntity

data class GenreDto(
    val id: Int,
    val name: String
) {
    constructor(genre: GenreEntity) : this(
        id = genre.id.toInt(),
        name = genre.name
    )
}
