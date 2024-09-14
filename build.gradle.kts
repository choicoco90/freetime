plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
}

group = "org.example"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-jpa")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("com.mysql:mysql-connector-j")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.data:spring-data-commons")
    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // koTest
    testImplementation("io.kotest:kotest-runner-junit5:5.3.0")
    testImplementation("io.kotest:kotest-assertions-core:5.3.0")
    testImplementation("io.kotest:kotest-property:5.3.0")
    testImplementation("io.kotest:kotest-framework-datatest:5.3.0")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.1")

    testImplementation("org.assertj:assertj-core:3.16.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
