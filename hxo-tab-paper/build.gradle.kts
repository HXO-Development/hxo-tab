import dev.slne.surf.api.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.hexoria.hxo.tab.PaperMain")
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
    compileOnly(project(":hxo-tab-api"))
}
