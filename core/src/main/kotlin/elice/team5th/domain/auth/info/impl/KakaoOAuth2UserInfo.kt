package elice.team5th.domain.auth.info.impl

import elice.team5th.domain.auth.info.OAuth2UserInfo

class KakaoOAuth2UserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return attributes["id"].toString()
    }

    override fun getName(): String {
        val properties = attributes["properties"] as Map<String, Any>
        return properties["nickname"] as String
    }

    override fun getEmail(): String? {
        return attributes["account.email"] as String?
    }

    override fun getImageUrl(): String {
        val properties = attributes["properties"] as Map<String, Any>
        return properties["thumbnail_image"] as String
    }
}
