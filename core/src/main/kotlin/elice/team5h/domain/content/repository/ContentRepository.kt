package elice.team5h.domain.content.repository

import elice.team5h.domain.content.model.Content
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository : JpaRepository<Content, Long>
