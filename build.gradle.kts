plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.springframework/spring-context
    implementation("org.springframework:spring-context:6.2.5")

    // https://mvnrepository.com/artifact/org.springframework/spring-aop
    implementation("org.springframework:spring-aop:6.2.5")

    // https://mvnrepository.com/artifact/org.aspectj/aspectjweaver
    implementation("org.aspectj:aspectjweaver:1.9.23")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-csv
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.18.3")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:2.0.17")

    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    runtimeOnly("ch.qos.logback:logback-classic:1.5.18")
}

tasks.test {
    useJUnitPlatform()
}