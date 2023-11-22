package elice.team5th.domain.auth.info

import elice.team5th.domain.auth.info.impl.GoogleOAuth2UserInfo
import elice.team5th.domain.auth.info.impl.KakaoOAuth2UserInfo
import elice.team5th.domain.auth.info.impl.NaverOAuth2UserInfo
import elice.team5th.domain.user.model.ProviderType

object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(
        providerType: ProviderType,
        attributes: Map<String, Any>
    ): OAuth2UserInfo {
        return when (providerType) {
            ProviderType.GOOGLE -> GoogleOAuth2UserInfo(attributes)
            ProviderType.KAKAO -> KakaoOAuth2UserInfo(attributes)
            ProviderType.NAVER -> NaverOAuth2UserInfo(attributes)
            else -> throw IllegalArgumentException("지원하지 않는 소셜 로그인입니다.")
        }
    }
}
