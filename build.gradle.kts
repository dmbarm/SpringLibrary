plugins {
    id("java")
    id("org.liquibase.gradle") version "2.2.2"
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

val dbChangelog = findProperty("DB_CHANGELOG")?.toString() ?: error("DB_CHANGELOG not set")
val dbUrl = System.getenv("DB_URL") ?: error("DB_URL not set")
val dbUser = System.getenv("DB_USER") ?: error("DB_USER not set")
val dbPass = System.getenv("DB_PASSWORD") ?: error("DB_PASSWORD not set")
val dbDriver = System.getenv("DB_DRIVER") ?: error("DB_DRIVER not set")

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to dbChangelog,
            "url" to dbUrl,
            "username" to dbUser,
            "password" to dbPass,
            "driverClassName" to dbDriver)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.spring)

    runtimeOnly(libs.bundles.database)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.bundles.test)

    liquibaseRuntime(libs.bundles.liquibase)
}

tasks.test {
    useJUnitPlatform()
}