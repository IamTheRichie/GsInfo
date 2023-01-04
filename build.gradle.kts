//import org.ajoberstar.reckon.core.Version

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.9-SNAPSHOT"
    id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing

    id("com.github.johnrengelman.shadow") version "7.1.2" // integrate dependencies
    id("maven-publish")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1" // Generates plugin.yml
}

group = "de.richie93"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://maven.enginehub.org/repo/") } // WorldGuard
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") } // FAWE
    /*maven {
      name = "S01 Sonatype Snapshots"
      url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
      url = uri("https://repo.md-5.net/content/repositories/public/")
    }
    maven {
      url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
      url = uri("org.sonatype.oss")
    }*/

    flatDir {
        dirs("extern_jars")
    }
}

dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.6-SNAPSHOT")

    implementation(platform("com.intellectualsites.bom:bom-1.18.x:1.20"))
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }


    // You will need to manually specify the full dependency if using the groovy gradle dsl
    // (paperDevBundle and paperweightDevBundle functions do not work in groovy)
    // paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.18.1-R0.1-SNAPSHOT")
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    reckonTagCreate {
        dependsOn(check)
    }
    publish {
        dependsOn(reckonTagPush)
    }
}




publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.richie93"
            artifactId = "gsinfo"

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/IamTheRichie/GsInfo")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

bukkit {
    main = "de.richie93.gsinfo.GsInfo"
    description = "A paper plugin that lists all entities and blocks on a GS/Worldedit ballot."
    version = getVersion().toString()
    apiVersion = "1.19"
    author = "Richie"
    depend = listOf("WorldEdit", "WorldGuard")
}
