plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter-batch")

    testImplementation("org.springframework.batch:spring-batch-test")
}

tasks.bootJar {
    archiveFileName.set("elice-batch.jar")
}
