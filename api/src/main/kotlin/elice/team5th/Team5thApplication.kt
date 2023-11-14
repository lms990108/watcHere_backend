package elice.team5th

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication()
@EntityScan("elice.team5th.domain.content")
class Team5thApplication

fun main(args: Array<String>) {
    runApplication<Team5thApplication>(*args)
}
