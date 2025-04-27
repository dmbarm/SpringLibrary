plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.context)

    implementation(libs.spring.aop)

    implementation(libs.aspectjweaver)

    implementation(libs.jackson.csv)

    implementation(libs.slf4j.api)

    runtimeOnly(libs.logback.classic)
}

tasks.test {
    useJUnitPlatform()
}