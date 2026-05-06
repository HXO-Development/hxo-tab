package dev.hiorcraft.nex.tab.command

import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistService
import dev.hiorcraft.nex.tab.util.PermissionRegistry
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Bukkit


fun nexTabCommand() = commandTree("nextab") {
    withAliases("tab")

    withPermission(PermissionRegistry.COMMAND_TAB)

    anyExecutor { executor, _ ->

        executor.sendText{
            appendInfoPrefix()
            secondary("Du kannst die Tablist mit /nextab reload neu laden.")
        }
    }

    literalArgument("reload")

    anyExecutor { executor, _ ->

        Bukkit.getAsyncScheduler().runNow(plugin) { _ ->
            Bukkit.getOnlinePlayers().forEach { player ->
                tablistService.sendAdditions(player)
                tablistService.formatPlayer(player)
            }
        }

        executor.sendText{
            appendInfoPrefix()
            success("Die Tablist wurde neu geladen")
        }
    }
}

