import org.gradle.kotlin.dsl.liquibaseRuntime

plugins {
    id("java")
    id("org.liquibase.gradle") version "2.2.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("org.liquibase:liquibase-core:4.25.1")

    liquibaseRuntime("org.liquibase:liquibase-core:4.23.2")
    liquibaseRuntime("com.mysql:mysql-connector-j:8.4.0")
    liquibaseRuntime("org.yaml:snakeyaml:2.2")
}


tasks.test {
    useJUnitPlatform()
}