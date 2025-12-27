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

val versions = mapOf(
    "junit" to "5.11.4",
    "junitPlatform" to "1.11.4",
    "mockito" to "5.14.2",
    "byteBuddy" to "1.15.11",
    "mysqlConnector" to "8.2.0",
    "liquibaseCore" to "4.25.1",
    "liquibaseRuntime" to "4.23.2",
    "mysqlConnectorRuntime" to "8.4.0",
    "snakeyaml" to "2.2"
)

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${versions["junit"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${versions["junit"]}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${versions["junitPlatform"]}")
    testImplementation("org.mockito:mockito-core:${versions["mockito"]}")
    testImplementation("org.mockito:mockito-junit-jupiter:${versions["mockito"]}")
    testImplementation("net.bytebuddy:byte-buddy:${versions["byteBuddy"]}")
    testImplementation("net.bytebuddy:byte-buddy-agent:${versions["byteBuddy"]}")

    implementation("com.mysql:mysql-connector-j:${versions["mysqlConnector"]}")
    implementation("org.liquibase:liquibase-core:${versions["liquibaseCore"]}")

    liquibaseRuntime("org.liquibase:liquibase-core:${versions["liquibaseRuntime"]}")
    liquibaseRuntime("com.mysql:mysql-connector-j:${versions["mysqlConnectorRuntime"]}")
    liquibaseRuntime("org.yaml:snakeyaml:${versions["snakeyaml"]}")
}


tasks.test {
    useJUnitPlatform()
}