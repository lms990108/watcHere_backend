package elice.team5th.domain.user.repository

import elice.team5th.domain.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUserId(userId: String): User?
    fun findUserByNickname(nickname: String): User?
    fun findUsersByBanIsTrue(pageable: Pageable): Page<User>
}
