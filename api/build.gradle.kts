plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":core"))
}

tasks.bootJar {
    archiveFileName.set("elice-api.jar")
}
