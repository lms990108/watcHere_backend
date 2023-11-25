package elice.team5th.domain.user.service

import elice.team5th.common.exception.FileSizeExceededException
import elice.team5th.common.service.S3Service
import elice.team5th.domain.auth.repository.UserRefreshTokenRepository
import elice.team5th.domain.user.dto.UserDto
import elice.team5th.domain.user.exception.UserNotFoundException
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import elice.team5th.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val s3Service: S3Service,
    private val userRepository: UserRepository,
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {
    fun findAllUsers(pageable: Pageable): Page<User> =
        userRepository.findAll(pageable)

    fun findUserById(userId: String): User =
        userRepository.findByUserId(userId) ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")

    fun findUserByNickname(nickname: String): User =
        userRepository.findByNickname(nickname) ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")

    fun checkDeletedUser(userId: String): Boolean =
        userRepository.findByUserId(userId)?.deletedAt != null

    fun updateUser(userId: String, nickname: String?, profileImage: MultipartFile?): User =
        userRepository.findByUserId(userId)?.apply {
            this.nickname = nickname ?: this.nickname
            if (profileImage != null) {
                if (profileImage.size > 2000000) throw FileSizeExceededException("프로필 이미지는 2MB를 넘을 수 없습니다.")
                this.profileImage = s3Service.uploadFile(profileImage)
            }
        }?.let { userRepository.save(it) }
            ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")

    fun updateBan(userId: String, ban: Boolean): User {
        return userRepository.findByUserId(userId)?.apply {
            this.ban = ban
        }?.let { userRepository.save(it) }
            ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")
    }

    fun updateRole(userId: String, role: RoleType): User {
        return userRepository.findByUserId(userId)?.apply {
            this.role = role
        }?.let { userRepository.save(it) }
            ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")
    }

    fun deleteUser(userId: String) {
        userRepository.findByUserId(userId)?.apply {
            deletedAt = java.time.LocalDateTime.now()
        }?.let { userRepository.save(it) }
            ?: throw UserNotFoundException("유저를 찾을 수 없습니다.")
        userRefreshTokenRepository.findByUserId(userId)?.let { userRefreshTokenRepository.delete(it) }
    }

    fun logout(userId: String) {
        userRefreshTokenRepository.findByUserId(userId)?.let { userRefreshTokenRepository.delete(it) }
    }

    fun findUsersByBanIsTrue(pageable: Pageable): Page<User> =
        userRepository.findByBanIsTrue(pageable)

    fun findUsersByNicknameStartingWith(pageable: Pageable, nicknamePrefix: String): Page<User> =
        userRepository.findByNicknameStartingWith(pageable, nicknamePrefix)

    fun findUsersByNicknameStartingWithAndBanIsTrue(pageable: Pageable, nicknamePrefix: String): Page<User> =
        userRepository.findByNicknameStartingWithAndBanIsTrue(pageable, nicknamePrefix)
}
