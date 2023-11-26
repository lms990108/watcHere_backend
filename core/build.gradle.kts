allOpen {
    annotation("jakarta.persistence.Entity")
}

noArg {
    annotation("jakarta.persistence.Entity")
}

dependencies {
    implementation("org.springframework.data:spring-data-redis")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.7")
}

