package elice.team5th.domain.tmdb.dto

data class SearchListResponseDto(
    val totalResult: Int,
    val results: List<ListInfoDto>
)
