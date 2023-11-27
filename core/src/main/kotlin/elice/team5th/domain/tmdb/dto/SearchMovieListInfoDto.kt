package elice.team5th.domain.tmdb.dto

data class SearchMovieListInfoDto(
    val id: Int,
    val title: String?, // 영화 제목
    val poster_path: String?
)
