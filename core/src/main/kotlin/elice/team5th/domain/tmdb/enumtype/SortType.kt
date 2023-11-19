package elice.team5th.domain.tmdb.enumtype

enum class SortType(val queryParam: String) {
    POPULARITY_DESC("popularity.desc"), // 인기순
    RELEASE_DATE_DESC("primary_release_date.desc"), // 최신순
    VOTE_AVERAGE_DESC("vote_average.desc") // 평점순
}
