package elice.team5th.domain.tmdb.dto

data class SearchTVListInfoDto(
    val id: Int,
    val name: String?, // TV 쇼 이름
    val poster_path: String?
)
