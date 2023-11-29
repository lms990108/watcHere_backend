package elice.team5th.common.service

import elice.team5th.config.logger
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val stringRedisTemplate: StringRedisTemplate
) {
    private val log = logger()

    fun getKeys(pattern: String): Set<String>? {
        try {
            log.debug("[CACHE KEYS] $pattern")
            return stringRedisTemplate.keys(pattern)
        } catch (e: Exception) {
            log.error(e.message ,e)
        }
        return null
    }

    // 문자열 데이터 저장 및 검색
    fun setValue(key: String, value: String) {
        try {
            log.debug("[CACHE SET] $key")
            stringRedisTemplate.opsForValue().set(key, value)
        } catch (e: Exception) {
            log.error(e.message ,e)
        }
    }

    fun getValue(key: String): String? {
        try {
            log.debug("[CACHE GET] $key")
            return stringRedisTemplate.opsForValue().get(key)
        } catch (e: Exception) {
            log.error(e.message ,e)
        }
        return null
    }

    fun deleteValue(key: String) {
        try {
            log.debug("[CACHE DELETE] $key")
            stringRedisTemplate.delete(key)
        } catch (e: Exception) {
            log.error(e.message ,e)
        }
    }

    // 객체 데이터 저장 및 검색
    fun setObject(key: String, value: Any) {
        try {
            log.debug("[CACHE SET] $key")
            redisTemplate.opsForValue().set(key, value)
        } catch (e: Exception) {
            log.error(e.message ,e)
        }
    }

    fun getObject(key: String): Any? {
        try {
            log.debug("[CACHE GET] $key")
            return redisTemplate.opsForValue().get(key)
        } catch (e: Exception) {
            log.error(e.message ,e)
        }
        return null
    }

    fun deleteObject(key: String) {
        try {
            log.debug("[CACHE DELETE] $key")
            redisTemplate.delete(key)
        } catch (e: Exception) {
            log.error(e.message ,e)
        }
    }
}
