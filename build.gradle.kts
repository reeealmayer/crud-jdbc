import org.gradle.kotlin.dsl.liquibaseRuntime

plugins {
    id("java")
    id("org.liquibase.gradle") version "2.2.0"
    id("io.freefair.lombok") version "9.1.0"
}

group = "kz.shyngys"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")
    testImplementation("net.bytebuddy:byte-buddy:1.15.11")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.15.11")

    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("org.liquibase:liquibase-core:4.25.1")

    liquibaseRuntime("org.liquibase:liquibase-core:4.23.2")
    liquibaseRuntime("com.mysql:mysql-connector-j:8.4.0")
    liquibaseRuntime("org.yaml:snakeyaml:2.2")
}


tasks.test {
    useJUnitPlatform()
}