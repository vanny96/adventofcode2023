group = "io.vanny96"
version = "2023"

plugins {
    kotlin("jvm") version "1.9.21"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.7.3")
    implementation("org.apache.commons", "commons-math3", "3.6.1")
    implementation("org.apache.commons", "commons-lang3", "3.14.0")
}

sourceSets {
    main {
        kotlin.srcDir("src/main/kotlin")
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}