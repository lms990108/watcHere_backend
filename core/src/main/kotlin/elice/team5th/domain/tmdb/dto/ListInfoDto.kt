package elice.team5th.domain.tmdb.dto

data class ListInfoDto(
    val id: Int,
    val title: String?, // 영화 제목
    val name: String?, // TV 쇼 이름
    val poster_path: String
)
