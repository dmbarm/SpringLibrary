plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.spring)
    runtimeOnly(libs.bundles.database)
    testImplementation(libs.bundles.test)
}

tasks.test {
    useJUnitPlatform()
}