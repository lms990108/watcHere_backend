package elice.team5th.domain.auth.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SecurityException
import java.lang.IllegalArgumentException
import java.security.Key
import java.util.Date

class AuthToken(
    val token: String?,
    private val key: Key
) {
    companion object {
        private const val AUTHORITIES_KEY = "role"

        fun createAuthToken(id: String, expiry: Date, key: Key): AuthToken {
            val token = Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact()
            return AuthToken(token, key)
        }

        fun createAuthToken(id: String, role: String, expiry: Date, key: Key): AuthToken {
            val token = Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact()
            return AuthToken(token, key)
        }
    }

    fun validate(): Boolean {
        return getTokenClaims() != null
    }

    fun getTokenClaims(): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SecurityException) {
            print("Invalid JWT signature.")
            throw e
        } catch (e: MalformedJwtException) {
            print("Invalid JWT token.")
            throw e
        } catch (e: ExpiredJwtException) {
            print("Expired JWT token.")
            throw e
        } catch (e: UnsupportedJwtException) {
            print("Unsupported JWT token.")
            throw e
        } catch (e: IllegalArgumentException) {
            print("JWT token compact of handler are invalid.")
            throw e
        }
    }

    fun getExpiredTokenClaims(): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
            null
        } catch (e: ExpiredJwtException) {
            print("Expired JWT token.")
            throw e
        }
    }
}
