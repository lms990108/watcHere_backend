package elice.team5th.domain.auth.service

import elice.team5th.domain.auth.entity.UserPrincipal
import elice.team5th.domain.auth.info.OAuth2UserInfo
import elice.team5th.domain.auth.info.OAuth2UserInfoFactory
import elice.team5th.domain.user.model.ProviderType
import elice.team5th.domain.user.model.RoleType
import elice.team5th.domain.user.model.User
import elice.team5th.domain.user.repository.UserRepository
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val user = super.loadUser(userRequest)
        return try {
            process(userRequest, user)
        } catch (e: AuthenticationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw InternalAuthenticationServiceException(e.message, e.cause)
        }
    }

    // 받은 유저 정보를 가지고 DB에 저장하고, UserPrincipal 객체를 반환한다.
    private fun process(userRequest: OAuth2UserRequest, user: OAuth2User): OAuth2User {
        val providerType = ProviderType.valueOf(
            userRequest.clientRegistration.registrationId.uppercase(Locale.getDefault())
        )
        val userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.attributes)
        var savedUser = userRepository.findByUserId(userInfo.getId())

        if (savedUser != null) {
            updateUser(savedUser, userInfo)
        } else {
            savedUser = createUser(userInfo, providerType)
        }

        return UserPrincipal.create(savedUser, userInfo.attributes)
    }

    private fun createUser(userinfo: OAuth2UserInfo, providerType: ProviderType): User {
        return User(
            userId = userinfo.getId(),
            nickname = userinfo.getName(),
            profileImage = userinfo.getImageUrl(),
            email = userinfo.getEmail(),
            providerType = providerType,
            role = RoleType.USER
        ).let { userRepository.saveAndFlush(it) }
    }

    private fun updateUser(user: User, userInfo: OAuth2UserInfo): User {
        userInfo.getName().let {
            if (it != user.nickname) {
                user.nickname = it
            }
        }

        userInfo.getImageUrl().let {
            if (it != user.profileImage) {
                user.profileImage = it
            }
        }

        return user
    }
}
