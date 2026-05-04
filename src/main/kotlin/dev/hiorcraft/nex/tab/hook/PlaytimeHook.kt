package dev.hiorcraft.nex.tab.hook

import dev.slne.surf.playtime.api.common.surfPlaytimeApi
import java.util.UUID

object PlaytimeHook {

    fun isAfk(playerUuid: UUID): Boolean {
        return surfPlaytimeApi.isPlayerAfk(playerUuid)
    }
}