package elice.team5th

import elice.team5th.config.properties.AppProperties
import elice.team5th.config.properties.CorsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication()
@EnableScheduling
@EnableConfigurationProperties(
    CorsProperties::class,
    AppProperties::class
)
class Team5thApplication

fun main(args: Array<String>) {
    runApplication<Team5thApplication>(*args)
}
