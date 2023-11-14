package elice.team5th.domain.content.repository

import elice.team5th.domain.content.model.Content
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ContentRepository : JpaRepository<Content, Long> {
    // 제목으로 콘텐츠 검색
    fun findByTitleContaining(title: String, pageable: Pageable): Page<Content>
}
