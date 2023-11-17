package elice.team5th

import elice.team5th.config.properties.AppProperties
import elice.team5th.config.properties.CorsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication()
@EnableConfigurationProperties(
    CorsProperties::class,
    AppProperties::class
)
class Team5thApplication

fun main(args: Array<String>) {
    runApplication<Team5thApplication>(*args)
}
