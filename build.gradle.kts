plugins {
    application
    java
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.70"

}

group = "com.github.stonewall"
version = "1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.twitter4j:twitter4j-core:4.0.7")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0") // JVM dependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

application {
    mainClassName = "RPDRelayKt"
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
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
