package dev.hexoria.hxo.tab.redis.event

import dev.slne.surf.redis.event.RedisEvent
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TabEntryUpdateRedisEvent(
    val toUpdateUuid: @Contextual UUID
) : RedisEvent()