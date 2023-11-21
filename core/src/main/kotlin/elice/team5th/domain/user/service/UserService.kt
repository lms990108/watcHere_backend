package elice.team5th.domain.user.service

import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.exception.UserNotFoundException
import elice.team5th.domain.user.model.User
import elice.team5th.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun findAllUsers(pageable: Pageable): Page<User> =
        userRepository.findAll(pageable)

    fun findUserById(userId: String): User =
        userRepository.findByUserId(userId) ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")

    fun updateUser(userId: String, updateRequest: UserDto.UpdateRequest): User =
        userRepository.findByUserId(userId)?.apply {
            ban = updateRequest.ban ?: ban
            role = updateRequest.role ?: role
            profileImage = updateRequest.profileImage ?: profileImage
            nickname = updateRequest.nickname ?: nickname
        }?.let { userRepository.save(it) }
            ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")

    fun findUsersByBanIsTrue(pageable: Pageable): Page<User> =
        userRepository.findByBanIsTrue(pageable)

    fun findUsersByNicknameStartingWith(pageable: Pageable, nicknamePrefix: String): Page<User> =
        userRepository.findByNicknameStartingWith(pageable, nicknamePrefix)

    fun findUsersByNicknameStartingWithAndBanIsTrue(pageable: Pageable, nicknamePrefix: String): Page<User> =
        userRepository.findByNicknameStartingWithAndBanIsTrue(pageable, nicknamePrefix)
}
