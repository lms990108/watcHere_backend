package elice.team5th.domain.content.service

import elice.team5th.domain.content.dto.ContentDetailDto
import elice.team5th.domain.content.dto.ContentToListDto
import elice.team5th.domain.content.repository.ContentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContentService(private val contentRepository: ContentRepository) {

    // 전체 콘텐츠를 ContentToListDto 형태로 조회 (페이지네이션 적용)
    @Transactional(readOnly = true)
    fun getAllContentSummaries(pageable: Pageable): Page<ContentToListDto> {
        return contentRepository.findAll(pageable).map { content ->
            ContentToListDto(
                id = content.id,
                title = content.title,
                posterImageUrl = content.posterImage
            )
        }
    }

    // 콘텐츠 상세 조회
    @Transactional(readOnly = true)
    fun getContentById(id: Long): ContentDetailDto? {
        return contentRepository.findById(id).map { content ->
            ContentDetailDto(
                id = content.id,
                type = content.type,
                title = content.title,
                posterImageUrl = content.posterImage,
                starRating = content.starRating ?: 0f,
                director = content.director,
                provider = content.provider
            )
        }.orElse(null)
    }

    // 제목으로 콘텐츠 검색 (페이지네이션 적용)
    @Transactional(readOnly = true)
    fun searchContentsByTitle(title: String, pageable: Pageable): Page<ContentToListDto> {
        if (title.length < 2) {
            throw IllegalArgumentException("검색어는 두 글자 이상이어야 합니다.")
        }

        return contentRepository.findByTitleContaining(title, pageable).map { content ->
            ContentToListDto(
                id = content.id,
                title = content.title,
                posterImageUrl = content.posterImage
            )
        }
    }
}
