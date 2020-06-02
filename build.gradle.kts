plugins {
    application
    java
    kotlin("jvm") version "1.3.72"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.twitter4j:twitter4j-core:4.0.7")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    testCompile("junit", "junit", "4.12")
}

application {
    mainClassName = "ScraperKt"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}