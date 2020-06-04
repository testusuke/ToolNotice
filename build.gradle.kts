plugins {
    id ("org.jetbrains.kotlin.jvm") version "1.3.72"
    id ("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

group = "net.testusuke"
version = "1.0.2"

repositories {
    mavenCentral()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    /*
    maven{
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
    maven{
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
     */
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "$group.open.toolnotice.Main"
    author = "testusuke"
    apiVersion = "1.15"
    commands {
        register("tn"){
            description = "general command"
        }
        register("toolnotice"){
            description = "general command"
        }
    }
}

dependencies {
    compile ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.destroystokyo.paper:paper-api:1.15.2-R0.1-SNAPSHOT")
}


val jar by tasks.getting(Jar::class) {
    from(configurations.compile.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
}

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava.srcDirs)
}