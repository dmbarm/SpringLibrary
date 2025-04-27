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
    implementation(libs.jackson.csv)
    implementation(libs.bundles.database)
}

tasks.test {
    useJUnitPlatform()
}