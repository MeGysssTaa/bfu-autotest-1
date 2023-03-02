plugins {
    kotlin("jvm") version "1.8.0"
    application
    id("io.qameta.allure") version "2.9.6"
}

group = "me.darksidecode.bfu.autotest"
version = "0.0.1"

repositories {
    mavenCentral()
}

val junitVersion = "5.7.0"

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.1.3")
    testImplementation("io.qameta.allure:allure-java-commons:2.13.8")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.3.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}
