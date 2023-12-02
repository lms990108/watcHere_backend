package elice.team5th.domain.tmdb.dto

data class SearchTVListResponseDto(
    val totalResult: Int,
    val results: List<TVDetailsDto>
)
