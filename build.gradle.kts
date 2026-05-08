import dev.slne.surf.api.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

group = "dev.hiorcraft.nex.tab"
version = findProperty("version") as String

surfPaperPluginApi {
    mainClass("dev.hiorcraft.nex.tab.PaperMain")
    generateLibraryLoader(false)

    serverDependencies {
        registerRequired("LuckPerms")
        registerRequired("MiniPlaceholders")
    }

    authors.add("HiorCraft")
}

dependencies {
    compileOnly("net.luckperms:api:5.4")
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.3.0")
}