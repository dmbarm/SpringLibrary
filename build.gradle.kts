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
    implementation(libs.aspectjweaver)
    implementation(libs.jackson.csv)
    implementation(libs.bundles.logging)
}

tasks.test {
    useJUnitPlatform()
}