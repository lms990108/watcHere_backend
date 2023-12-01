package elice.team5th.domain.user.service

import elice.team5th.common.exception.FileSizeExceededException
import elice.team5th.common.service.S3Service
import elice.team5th.domain.auth.repository.UserRefreshTokenRepository
import elice.team5th.domain.auth.token.UserRefreshToken
import elice.team5th.domain.user.exception.UserNotFoundException
import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import elice.team5th.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDateTime

internal class UserServiceTest: StringSpec({

    // mock 객체 생성
    val userRepository = mockk<UserRepository>()
    val s3Service = mockk<S3Service>()
    val userRefreshTokenRepository = mockk<UserRefreshTokenRepository>()
    val userService = UserService(s3Service, userRepository, userRefreshTokenRepository)

    "존재하지 않는 유저를 찾으면 예외를 발생시킨다" {
        val userId = "유저1"

        every { userRepository.findByUserId(userId) } returns null

        shouldThrow<UserNotFoundException> {
            userService.findUserById(userId)
        }
    }

    "닉네임과 포스터 정보 변경에 성공한다" {
        val userId = "유저1"
        val newNickname = "새로운 닉네임"
        val newPoster = "새로운 포스터 이미지"
        val user = User(
            userId = userId,
            nickname = "기존 닉네임",
            poster = "기존 포스터 이미지",
            profileImage = "프로필 이미지",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd1234@gmail.com",
            ban = false,
            deletedAt = null
        )

        /*
         * Stubbing
         * userRepository.findByUserId(userId) 메서드가 호출되면 user 객체를 반환하도록 설정
         * userRepository.save(any()) 메서드가 호출되면 firstArg<User>() 객체를 반환하도록 설정
         */
        every { userRepository.findByUserId(userId) } returns user
        every { userRepository.save(any()) } answers {
          firstArg<User>().apply {
              nickname = newNickname
              poster = newPoster
          }
        }

        val updatedUser = userService.updateUser(userId, newNickname, newPoster, null)

        // verify
        updatedUser.nickname shouldBe newNickname
        updatedUser.poster shouldBe newPoster
    }

    "프로필 이미지 변경에 성공한다" {
        val userId = "유저1"
        val newProfileImage = MockMultipartFile(
            "profileImage",
            "profileImage.jpg",
            "image/jpg",
            "새로운 프로필 이미지".toByteArray()
        )
        val uploadedImageUrl = "새로운 프로필 이미지 링크"
        val user = User(
            userId = userId,
            nickname = "기존 닉네임",
            poster = "기존 포스터 이미지",
            profileImage = "프로필 이미지",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd1234@gmail.com",
            ban = false,
            deletedAt = null
        )

        every { userRepository.findByUserId(userId) } returns user
        every { s3Service.uploadFile(newProfileImage) } returns uploadedImageUrl
        every { userRepository.save(any()) } answers {
            firstArg<User>().apply { profileImage = uploadedImageUrl }
        }

        val updatedUser = userService.updateUser(userId, null, null, newProfileImage)

        updatedUser.profileImage shouldBe uploadedImageUrl
    }

    "업로드하는 프로필 이미지 크기가 2MB를 초과하면 예외를 발생시킨다" {
        val userId = "유저1"
        val newProfileImage = MockMultipartFile(
            "profileImage",
            "profileImage.jpg",
            "image/jpg",
            ByteArray(3000000) // 3MB
        )
        val user = User(
            userId = userId,
            nickname = "기존 닉네임",
            poster = "기존 포스터 이미지",
            profileImage = "프로필 이미지",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd1234@gmail.com",
            ban = false,
            deletedAt = null
        )

        every { userRepository.findByUserId(userId) } returns user
        every { s3Service.uploadFile(any()) } returns "새로운 프로필 이미지"

        shouldThrow<FileSizeExceededException> {
            userService.updateUser(userId, null, null, newProfileImage)
        }
    }

    "유저 밴에 성공한다" {
        val userId = "유저1"
        val user = User(
            userId = userId,
            nickname = "닉네임",
            poster = "포스터 이미지",
            profileImage = "프로필 이미지",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd1234@gmail.com",
            ban = false,
            deletedAt = null
        )

        every { userRepository.findByUserId(userId) } returns user
        every { userRepository.save(any()) } answers { firstArg<User>() }

        val updatedUser = userService.updateBan(userId, true)

        updatedUser.ban shouldBe true
    }

    "유저 소프트 삭제에 성공한다" {
        val userId = "유저1"
        val user = User(
            userId = userId,
            nickname = "닉네임",
            poster = "포스터 이미지",
            profileImage = "프로필 이미지",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd1234@gmail.com",
            ban = false,
            deletedAt = null
        )
        val refreshToken = UserRefreshToken(userId, "refreshToken")

        every { userRepository.findByUserId(userId) } returns user
        every { userRepository.save(any()) } answers { firstArg<User>() }
        every { userRefreshTokenRepository.findByUserId(userId) } returns refreshToken
        every { userRefreshTokenRepository.delete(refreshToken) } just runs

        userService.deleteUser(userId)

        user.deletedAt.shouldNotBeNull()

        // 각 메서드가 정확히 1번씩 호출되었는지 검증
        verify(exactly = 1) { userRepository.save(user) }
        verify(exactly = 1) { userRefreshTokenRepository.delete(refreshToken) }
    }

    "닉네임이 주어진 문자열로 시작하는 유저를 찾는다" {
        val nicknamePrefix = "닉네임"
        val pageable = PageRequest.of(0, 10)
        val user1 = User(
            userId = "유저1",
            nickname = "닉네임이 주어진 문자열로 시작하는 유저1",
            poster = "포스터 이미지1",
            profileImage = "프로필 이미지1",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd1234@gmail.com",
            ban = false,
            deletedAt = null
        )
        val user2 = User(
            userId = "유저2",
            nickname = "닉네임이 주어진 문자열로 시작하는 유저2",
            poster = "포스터 이미지2",
            profileImage = "프로필 이미지2",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd5678@gmail.com",
            ban = false,
            deletedAt = null
        )
        val userNotStartingGivenNickname = User(
            userId = "유저3",
            nickname = "nickname이 주어진 문자열로 시작하지 않는 유저",
            poster = "포스터 이미지3",
            profileImage = "프로필 이미지3",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "efgh1234@gmail.com",
            ban = false,
            deletedAt = null
        )
        val usersStartingGivenNickname = listOf(user1, user2)
        val pageOfUsers: Page<User> =
            PageImpl(usersStartingGivenNickname, pageable, usersStartingGivenNickname.size.toLong())

        every { userRepository.findByNicknameStartingWith(pageable, nicknamePrefix) } returns pageOfUsers

        val result = userService.findUsersByNicknameStartingWith(pageable, nicknamePrefix)

        result.totalElements shouldBe usersStartingGivenNickname.size.toLong()
        result.content.forEach { it.nickname.startsWith(nicknamePrefix) shouldBe true }
    }

    "소프트 삭제된 유저가 있는 유저들 중 삭제된 유저는 포함하지 않은 채로 닉네임이 주어진 문자열로 시작하는 유저들을 찾는다" {
        val nicknamePrefix = "닉네임"
        val pageable = PageRequest.of(0, 10)
        val user1 = User(
            userId = "유저1",
            nickname = "닉네임이 주어진 문자열로 시작하는 유저1",
            poster = "포스터 이미지1",
            profileImage = "프로필 이미지1",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd1234@gmail.com",
            ban = false,
            deletedAt = null
        )
        val user2 = User(
            userId = "유저2",
            nickname = "닉네임이 주어진 문자열로 시작하는 유저2",
            poster = "포스터 이미지2",
            profileImage = "프로필 이미지2",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "abcd5678@gmail.com",
            ban = false,
            deletedAt = null
        )
        val deletedUser = User(
            userId = "유저3",
            nickname = "닉네임이 주어진 문자열로 시작하는 소프트 삭제된 유저",
            poster = "포스터 이미지3",
            profileImage = "프로필 이미지3",
            role = RoleType.USER,
            provider = ProviderType.GOOGLE,
            email = "efgh1234@gmail.com",
            ban = false,
            deletedAt = LocalDateTime.now()
        )
        val usersStartingGivenNickname = listOf(user1, user2)
        val pageOfUsers: Page<User> =
            PageImpl(usersStartingGivenNickname, pageable, usersStartingGivenNickname.size.toLong())

        every { userRepository.findByNicknameStartingWith(pageable, nicknamePrefix) } returns pageOfUsers

        val result = userService.findUsersByNicknameStartingWith(pageable, nicknamePrefix)

        result.totalElements shouldBe usersStartingGivenNickname.size.toLong()
        result.content.forEach {
            it.nickname.startsWith(nicknamePrefix) shouldBe true
            it.deletedAt.shouldBeNull()
        }
    }
})
