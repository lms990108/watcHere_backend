package elice.team5th.controller

import elice.team5th.domain.content.service.ContentService
import elice.team5th.domain.content.model.Content
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@ComponentScan("elice.team5th.domain")
@RequestMapping("/contents")
class ContentController(private val contentService: ContentService) {

    @GetMapping
    fun getAllContents(): ResponseEntity<List<Content>> {
        val contents = contentService.findAllContents()
        return ResponseEntity.ok(contents)
    }

    @GetMapping("/{id}")
    fun getContentById(@PathVariable id: Long): ResponseEntity<Content> {
        val content = contentService.findContentById(id)
        return if (content != null) ResponseEntity.ok(content) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createContent(@RequestBody content: Content): ResponseEntity<Content> {
        val newContent = contentService.createContent(content)
        return ResponseEntity.ok(newContent)
    }

    @PutMapping("/{id}")
    fun updateContent(@PathVariable id: Long, @RequestBody content: Content): ResponseEntity<Content> {
        return try {
            val updatedContent = contentService.updateContent(id, content)
            ResponseEntity.ok(updatedContent)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteContent(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            contentService.deleteContent(id)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}
