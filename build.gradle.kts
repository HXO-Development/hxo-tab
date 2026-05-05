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
        registerSoft("MiniPlaceholders")
        registerSoft("PlaceholderAPI")
    }
}

dependencies {
    compileOnly("net.luckperms:api:5.4")
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.2.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
}