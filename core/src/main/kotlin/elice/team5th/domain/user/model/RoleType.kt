package elice.team5th.domain.user.model

enum class RoleType(val code: String, val displayName: String) {
    GUEST("ROLE_GUEST", "게스트"),
    USER("ROLE_USER", "유저"),
    ADMIN("ROLE_ADMIN", "관리자");

    companion object {
        fun of(code: String): RoleType = values().find { it.code == code } ?: GUEST
    }
}
