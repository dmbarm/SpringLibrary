plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.io.spring.dependency.management)
    alias(libs.plugins.liquibase.gradle)
}

group = "org.example"
version = "1.0-SNAPSHOT"

val javaVersion = findProperty("JAVA_VERSION")?.toString()?.toInt()
    ?: error("Java version is not set")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

val logLevel = findProperty("LOG_LEVEL")?.toString() ?: error("LOG_LEVEL not set")
val dbChangelog = findProperty("DB_CHANGELOG")?.toString() ?: error("DB_CHANGELOG not set")
val dbUrl = findProperty("DB_URL")?.toString() ?: error("DB_URL not set")
val dbUser = findProperty("DB_USER")?.toString() ?: error("DB_USER not set")
val dbPass = findProperty("DB_PASSWORD")?.toString() ?: error("DB_PASSWORD not set")

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to logLevel,
            "changelogFile" to dbChangelog,
            "url" to dbUrl,
            "username" to dbUser,
            "password" to dbPass)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.spring)
    implementation(libs.bundles.jwt)

    runtimeOnly(libs.bundles.database)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.bundles.test)

    liquibaseRuntime(libs.bundles.liquibase)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("refreshDatabase") {
    group = "development"
    description = "Drops and recreated the whole database. Used only in DEVELOPMENT matters"
    dependsOn("dropAll")
    finalizedBy("update")
}