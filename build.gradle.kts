import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    id("fabric-loom") version "1.9-SNAPSHOT"
}

base {
    archivesName = properties["archives_name"] as String
    val commitHash = System.getenv("GITHUB_SHA")?.toString()?.substring(0..6) ?: "unknown"
    project.ext.set("commit-hash", commitHash)
    version = properties["mod_version"] as String + '+' + commitHash
    group = properties["maven_group"] as String
}

repositories {
    // meteor
    maven("https://maven.meteordev.org/releases")
    maven("https://maven.meteordev.org/snapshots")

    // jitpack, currently only used for viafabricplus
    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://jitpack.io")
            }
        }
        filter {
            includeGroup("com.github.Oryxel")
        }
    }
}

loom {
    accessWidenerPath = file("src/main/resources/antip2w.accesswidener")
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api_version"]}")
    modImplementation("meteordevelopment:meteor-client:${properties["meteor_version"]}")
}

tasks {
    processResources {
        val buildTime = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm'Z'")
            .withZone(ZoneOffset.UTC)
            .format(Instant.now());

        val properties = mapOf(
            "version"           to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "commit_hash"       to project.ext.get("commit-hash"),
            "build_time"        to buildTime
        )

        inputs.properties(properties)
        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName.get()}" }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        withSourcesJar()
    }

    withType<JavaCompile> {
        options.release = 21
    }
}
