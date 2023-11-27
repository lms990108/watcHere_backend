package elice.team5th.domain.tmdb.dto

data class SearchMovieListResponseDto(
    val totalResult: Int,
    val results: List<SearchMovieListInfoDto>
)
