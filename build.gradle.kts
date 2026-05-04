import dev.slne.surf.surfapi.gradle.util.registerRequired
import dev.slne.surf.surfapi.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin") version "1.21.11+"
}

version = findProperty("version") as String
group = "de.hiorcraft.nex"

surfPaperPluginApi {
    mainClass("dev.hiorcraft.nex.tab.PaperMain")
    generateLibraryLoader(false)
    foliaSupported(true)

    authors.add("Hiorcraft")

    serverDependencies {
        registerRequired("LuckPerms")
        registerSoft("surf-playtime-paper")
    }
}

dependencies {
    compileOnly("net.luckperms:api:5.4")
    compileOnly("dev.slne.surf.playtime:surf-playtime-api-paper:+")
}