plugins {
    application
    java
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.serialization") version "1.4.0"

}

group = "com.github.stonewall"
version = "2.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.twitter4j:twitter4j-core:4.0.7")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.jsoup:jsoup:1.18.1")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

application {
    mainClassName = "RPDRelayKt"
}


tasks {
    test {
        doFirst {
            processTestResources
        }
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
