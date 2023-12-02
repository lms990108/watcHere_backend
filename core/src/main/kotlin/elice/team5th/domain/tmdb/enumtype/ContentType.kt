package elice.team5th.domain.tmdb.enumtype

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class ContentType {
    MOVIE,
    TV;

    // 필요한 경우 JsonValue 또는 JsonCreator 사용
    @JsonValue
    fun toValue(): String {
        return this.name
    }

    companion object {
        @JsonCreator
        fun forValue(value: String): ContentType {
            return ContentType.valueOf(value.toUpperCase())
        }
    }
}
