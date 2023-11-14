package elice.team5th.domain.content.repository

import elice.team5th.domain.content.model.Content
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository : JpaRepository<Content, Long>
