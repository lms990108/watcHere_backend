import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.5" apply false
    id("io.spring.dependency-management") version "1.1.3"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.allopen") version "1.8.22"
    kotlin("plugin.noarg") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "elice"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.jpa")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("io.spring.dependency-management")
        plugin("kotlin-spring")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.1.3")
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-websocket")
        implementation("org.springframework.security:spring-security-messaging")
        implementation("org.springframework.boot:spring-boot-starter-log4j2")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        compileOnly("io.jsonwebtoken:jjwt-api:0.11.2")
        runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.amazonaws:aws-java-sdk-s3:1.12.583")
        runtimeOnly("com.mysql:mysql-connector-j")

        testImplementation(platform("org.junit:junit-bom:5.10.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("io.mockk:mockk:1.13.8")
        testImplementation("io.kotest:kotest-runner-junit5:5.7.0")
        testImplementation("io.kotest:kotest-assertions-core:5.7.0")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configurations {
        all {
            exclude(module = "spring-boot-starter-logging")
            exclude(module = "logback-classic")
            exclude(module = "logback-core")
        }
    }
}
