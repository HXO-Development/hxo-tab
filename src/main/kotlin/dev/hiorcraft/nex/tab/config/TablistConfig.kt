package dev.hiorcraft.nex.tab.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class TablistConfig(
    val header: String = "<br><br><br>\uE238<br><br>",
    val footer: String = "<br><#59CCF2>Du bist auf <server><br><gray>ʜᴇxᴏʀɪᴀ.ɴᴇᴛ<br>",
    val serverName: String = "UNKNOWN"
)