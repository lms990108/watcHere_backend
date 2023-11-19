package elice.team5th.domain.auth.info.impl

import elice.team5th.domain.auth.info.OAuth2UserInfo

class NaverOAuth2UserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        val response = attributes["response"] as Map<String, Any>
        return response["id"] as String
    }

    override fun getName(): String {
        val response = attributes["response"] as Map<String, Any>
        return response["nickname"] as String
    }

    override fun getEmail(): String {
        val response = attributes["response"] as Map<String, Any>
        return response["email"] as String
    }

    override fun getImageUrl(): String {
        val response = attributes["response"] as Map<String, Any>
        return response["profile_image"] as String
    }
}
