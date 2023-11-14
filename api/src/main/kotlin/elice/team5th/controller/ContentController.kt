package elice.team5th.domain.content.controller

import elice.team5th.domain.content.dto.ContentResponseDTO
import elice.team5th.domain.content.dto.CreateContentDTO
import elice.team5th.domain.content.dto.UpdateContentDTO
import elice.team5th.domain.content.service.ContentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contents")
class ContentController(private val contentService: ContentService) {

    @PostMapping
    fun createContent(@RequestBody createDto: CreateContentDTO): ResponseEntity<ContentResponseDTO> {
        val content = contentService.createContent(createDto)
        return ResponseEntity.ok(content)
    }

    @GetMapping("/{id}")
    fun getContent(@PathVariable id: Long): ResponseEntity<ContentResponseDTO> {
        val content = contentService.getContent(id)
        return ResponseEntity.ok(content)
    }

    @PutMapping("/{id}")
    fun updateContent(@PathVariable id: Long, @RequestBody updateDto: UpdateContentDTO): ResponseEntity<ContentResponseDTO> {
        val updatedContent = contentService.updateContent(id, updateDto)
        return ResponseEntity.ok(updatedContent)
    }

    @DeleteMapping("/{id}")
    fun deleteContent(@PathVariable id: Long): ResponseEntity<Void> {
        contentService.deleteContent(id)
        return ResponseEntity.noContent().build()
    }

    // Additional endpoints can be added here
}
