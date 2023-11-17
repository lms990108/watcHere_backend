package elice.team5th.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.Base64
import java.util.Optional

object CookieUtil {
    fun getCookie(request: HttpServletRequest, name: String): Optional<Cookie> {
        val cookies = request.cookies

        if (cookies.isNotEmpty()) {
            for (cookie in cookies) {
                if (cookie.name == name) {
                    return Optional.of(cookie)
                }
            }
        }
        return Optional.empty()
    }

    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        val cookies = request.cookies

        if (cookies.isNotEmpty()) {
            for (cookie in cookies) {
                if (cookie.name == name) {
                    cookie.value = ""
                    cookie.path = "/"
                    cookie.maxAge = 0
                    response.addCookie(cookie)
                }
            }
        }
    }

    fun serialize(obj: Any): String {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(obj))
    }

    inline fun <reified T> deserialize(cookie: Cookie): T? {
        return try {
            val bytes = Base64.getUrlDecoder().decode(cookie.value)
            val deserializedObject = SerializationUtils.deserialize(bytes)
            if (deserializedObject is T) {
                deserializedObject
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
