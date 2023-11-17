package elice.team5th.controller

import elice.team5th.domain.content.dto.ContentDetailDto
import elice.team5th.domain.content.dto.ContentToListDto
import elice.team5th.domain.content.service.ContentService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/contents")
class ContentController(private val contentService: ContentService) {

    // 전체 콘텐츠 조회
    @GetMapping
    fun getAllContentSummaries(pageable: Pageable): ResponseEntity<Page<ContentToListDto>> {
        val contentSummaries = contentService.getAllContentSummaries(pageable)
        return ResponseEntity.ok(contentSummaries)
    }

    // 콘텐츠 상세 조회
    @GetMapping("/{id}")
    fun getContentById(@PathVariable id: Long): ResponseEntity<ContentDetailDto> {
        val content = contentService.getContentById(id)
        return content?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    // 제목으로 콘텐츠 검색
    @GetMapping("/search")
    fun searchContentsByTitle(@RequestParam title: String, pageable: Pageable): ResponseEntity<Page<ContentToListDto>> {
        val contents = contentService.searchContentsByTitle(title, pageable)
        return ResponseEntity.ok(contents)
    }
}

