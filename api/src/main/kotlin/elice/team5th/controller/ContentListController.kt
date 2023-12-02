package elice.team5th.controller

import elice.team5th.domain.tmdb.dto.ListResponseDto
import elice.team5th.domain.tmdb.enumtype.ContentType
import elice.team5th.domain.tmdb.enumtype.SortType
import elice.team5th.domain.tmdb.service.ContentListService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/api/v1/contents")
class ContentListController(private val contentListService: ContentListService) {

    @GetMapping("")
    @Operation(
        summary = "컨텐츠 리스트 조회",
        description = "지정된 페이지, 정렬 방식, 제공자 및 콘텐츠 유형에 따라 인기 콘텐츠를 조회. \n" +
            "예시 : http://localhost:8080/api/v1/contents?page=2&sort=POPULARITY_DESC&provider=NETFLIX&contentType=TV&anime=true \n" +
            "1. 정렬방식 \n" +
            "POPULARITY_DESC: 인기순 \n" +
            "RELEASE_DATE_DESC: 최신순 \n" +
            "VOTE_AVERAGE_DESC: 평점순 \n" +
            "2. OTT \n" +
            "BOX_OFFICE \n" +
            "NETFLIX \n" +
            "WATCHA \n" +
            "DISNEY_PLUS \n" +
            "WAVVE \n" +
            "PARAMOUNT_PLUS \n" +
            "3. 컨텐츠 타입 <필수> : MOVIE or TV \n" +
            "4. 애니메이션 컨텐츠 여부 <선택> : true or false (default: false)"

    )
    fun getPopularContent(
        @RequestParam(required = false, defaultValue = "1") page: Int,
        @RequestParam(required = false, defaultValue = "POPULARITY_DESC") sort: String,
        @RequestParam(required = false, defaultValue = "NETFLIX") provider: String,
        @RequestParam(required = true) contentType: ContentType, // 영화 또는 TV 시리즈 선택
        @RequestParam(required = false, defaultValue = "false") anime: Boolean // 애니메이션 여부 추가
    ): Mono<ResponseEntity<ListResponseDto>> {
        val sortType = SortType.valueOf(sort.uppercase(Locale.getDefault()))
        return contentListService.getPopularContent(page, sortType, provider, contentType, anime)
            .map { ResponseEntity.ok(it) }
    }
}
