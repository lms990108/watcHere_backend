package elice.team5th.domain.content.service

import elice.team5th.domain.content.dto.ContentResponseDTO
import elice.team5th.domain.content.dto.CreateContentDTO
import elice.team5th.domain.content.dto.UpdateContentDTO
import elice.team5th.domain.content.model.Content
import elice.team5th.domain.content.repository.ContentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContentService(private val contentRepository: ContentRepository) {

    @Transactional
    fun createContent(createDto: CreateContentDTO): ContentResponseDTO {
        val content = Content().apply {
            title = createDto.title
            type = createDto.type
            posterImage = createDto.posterImage
            starRating = createDto.starRating
            director = createDto.director
            genre = createDto.genre
            releaseDate = createDto.releaseDate
            episodeDate = createDto.episodeDate
            season = createDto.season
        }
        return ContentResponseDTO.fromEntity(contentRepository.save(content))
    }

    @Transactional(readOnly = true)
    fun getContent(id: Long): ContentResponseDTO {
        val content = contentRepository.findById(id).orElseThrow {
            NoSuchElementException("Content with ID $id not found")
        }
        return ContentResponseDTO.fromEntity(content)
    }

    @Transactional
    fun updateContent(id: Long, updateDto: UpdateContentDTO): ContentResponseDTO {
        val content = contentRepository.findById(id).orElseThrow {
            NoSuchElementException("Content with ID $id not found")
        }

        updateDto.title?.let { content.title = it }
        updateDto.type?.let { content.type = it }
        updateDto.posterImage?.let { content.posterImage = it }
        updateDto.starRating?.let { content.starRating = it }
        updateDto.director?.let { content.director = it }
        updateDto.genre?.let { content.genre = it }
        updateDto.releaseDate?.let { content.releaseDate = it }
        updateDto.episodeDate?.let { content.episodeDate = it }
        updateDto.season?.let { content.season = it }

        return ContentResponseDTO.fromEntity(contentRepository.save(content))
    }

    @Transactional
    fun deleteContent(id: Long) {
        if (!contentRepository.existsById(id)) {
            throw NoSuchElementException("Content with ID $id not found")
        }
        contentRepository.deleteById(id)
    }

    // Additional service methods can be added here
}
