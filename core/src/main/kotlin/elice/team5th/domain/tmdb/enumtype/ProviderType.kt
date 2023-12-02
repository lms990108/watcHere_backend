package elice.team5th.domain.tmdb.enumtype

enum class ProviderType(val providerName: String, val providerId: Int) {
    BOX_OFFICE("BOX_OFFICE", 54),
    NETFLIX("NETFLIX", 8),
    WATCHA("WATCHA", 97),
    DISNEY_PLUS("DISNEY_PLUS", 337),
    WAVVE("WAVVE", 356),
    PARAMOUNT_PLUS("PARAMOUNT_PLUS", 582);

    companion object {
        fun getIdByName(name: String): Int? {
            return values().find { it.providerName.equals(name, ignoreCase = true) }?.providerId
        }
    }
}
