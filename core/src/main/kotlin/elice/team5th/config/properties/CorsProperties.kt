package elice.team5th.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import kotlin.properties.Delegates

@Component
@ConfigurationProperties(prefix = "cors")
class CorsProperties {
    lateinit var allowedOrigins: String
    lateinit var allowedMethods: String
    lateinit var allowedHeaders: String
    var maxAge by Delegates.notNull<Long>()
}
