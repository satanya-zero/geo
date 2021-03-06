import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm") version "1.3.61"
    id("com.github.johnrengelman.shadow").version("5.2.0")
}

group = "dev.glycine"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.locationtech.org/content/groups/releases")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
    compileOnly("com.destroystokyo.paper", "paper-api", "1.15.2-R0.1-SNAPSHOT")
    compileOnly("com.github.hazae41", "mc-kutils", "master-SNAPSHOT")
    implementation("org.locationtech.jts", "jts-core", "1.16.1")
    implementation("org.locationtech.jts.io", "jts-io-common", "1.16.1")
    implementation("org.mongodb", "mongodb-driver-sync", "3.12.0")
}

tasks {
    processResources {
        with(copySpec {
            from("src/main/resources")
            filter<ReplaceTokens>("tokens" to mapOf("version" to version))
        })
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "12"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "12"
    }
}
