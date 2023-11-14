package elice.team5th.domain.content.service

import elice.team5th.domain.content.model.Content
import elice.team5th.domain.content.repository.ContentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContentService(private val contentRepository: ContentRepository) {

    @Transactional(readOnly = true)
    fun findAllContents(): List<Content> {
        return contentRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findContentById(id: Long): Content? {
        return contentRepository.findById(id).orElse(null)
    }

    @Transactional
    fun createContent(newContent: Content): Content {
        return contentRepository.save(newContent)
    }

    @Transactional
    fun updateContent(id: Long, updatedContent: Content): Content {
        val content = findContentById(id) ?: throw Exception("Content not found")
        // Update content's properties
        content.title = updatedContent.title
        content.type = updatedContent.type
        content.posterImage = updatedContent.posterImage
        content.starRating = updatedContent.starRating
        content.director = updatedContent.director
        return contentRepository.save(content)
    }

    @Transactional
    fun deleteContent(id: Long) {
        val content = findContentById(id) ?: throw Exception("Content not found")
        contentRepository.delete(content)
    }
}
