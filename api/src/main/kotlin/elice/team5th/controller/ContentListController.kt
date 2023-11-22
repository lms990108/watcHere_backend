package elice.team5th.controller

import elice.team5th.domain.tmdb.dto.ListResponseDto
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.tmdb.enumtype.SortType
import elice.team5th.domain.tmdb.service.ContentListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

/**
 * 콘텐츠 목록 컨트롤러
 *
 * 이 컨트롤러는 정렬 및 필터링 옵션을 가진 영화 및 TV 시리즈 목록 요청을 처리합니다.
 *
 * 엔드포인트: /api/content-list/
 * 메서드: GET
 *
 * 요청 파라미터:
 * - page (선택적, Int): 가져올 페이지 번호입니다. 기본값은 1입니다.
 * - sort (선택적, String): 목록의 정렬 기준입니다.
 *   가능한 값은 "POPULARITY_DESC"(기본값), "RELEASE_DATE_DESC", "VOTE_AVERAGE_DESC" 등이 있습니다.
 * - provider (선택적, String): 콘텐츠의 서비스 제공업체입니다.
 *   예시로는 "NETFLIX", "WATCHA" 등이 있습니다.
 * - contentType (필수, ContentType): 콘텐츠 타입(영화 또는 TV 시리즈).
 *
 * 응답:
 * - ResponseEntity<ListResponseDto>: 콘텐츠 목록을 포함하는 응답 엔티티입니다.
 *   ListResponseDto는 ListInfoDto 객체의 목록을 포함하며, 각각 하나의 콘텐츠를 나타냅니다.
 *   각 ListInfoDto에는 콘텐츠 ID, 제목, 포스터 경로가 포함됩니다.
 *
 * 기능:
 * - 제공된 페이지 번호, 정렬 기준, 서비스 제공업체, 콘텐츠 타입을 기반으로 TMDB API에서 인기 콘텐츠 목록을 가져옵니다.
 *
 * 예시 요청:
 * GET /api/content-list?page=2&sort=RELEASE_DATE_DESC&provider=NETFLIX&contentType=MOVIE
 * - 넷플릭스에서 제공되는 영화 중 출시일 기준 내림차순으로 정렬된 두 번째 페이지의 영화 목록을 가져옵니다.
 */
@RestController
@RequestMapping("/api/v1/content-list")
class ContentListController(private val contentListService: ContentListService) {

    @GetMapping("")
    fun getPopularContent(
        @RequestParam(required = false, defaultValue = "1") page: Int,
        @RequestParam(required = false, defaultValue = "POPULARITY_DESC") sort: String,
        @RequestParam(required = false, defaultValue = "NETFLIX") provider: String,
        @RequestParam(required = true) contentType: ContentType // 영화 또는 TV 시리즈 선택
    ): Mono<ResponseEntity<ListResponseDto>> {
        val sortType = SortType
            .values()
            .find { it.name == sort.uppercase(Locale.getDefault()) } ?: SortType.POPULARITY_DESC
        return contentListService.getPopularContent(page, sortType, provider, contentType)
            .map { ResponseEntity.ok(it) }
    }
}

