plugins {
    `maven-publish`
    kotlin("jvm") version "1.6.20-M1"
    kotlin("plugin.serialization") version "1.4.0"

}

group = "com.github.stonewall"
version = "4.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
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
tasks.kotlinSourcesJar {
    archiveClassifier.set("sources")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            group = project.group.toString()
            artifactId = "rvalerts"
            version = project.version.toString()
            from(components["java"])
            artifact(tasks.kotlinSourcesJar)
        }
    }
}
