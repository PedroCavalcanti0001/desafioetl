val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposedVersion: String by project
plugins {
    application
    kotlin("jvm") version "1.6.10"
}

group = "pedroeugenio.me"
version = "0.0.1"
application {
    mainClass.set("pedroeugenio.me.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.zaxxer:HikariCP:4.0.0")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("io.ktor:ktor-gson:$ktor_version")

    runtimeOnly("mysql:mysql-connector-java:5.1.49")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}