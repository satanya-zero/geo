import enums.PropertyType
import handlers.handleSettingZone
import hazae41.minecraft.kutils.bukkit.BukkitEventPriority
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.listen
import hazae41.minecraft.kutils.bukkit.schedule
import managers.SessionManager
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

fun registerListeners(plugin: BukkitPlugin){
    plugin.listen<AsyncPlayerChatEvent>(BukkitEventPriority.LOWEST) { e ->
        if (SessionManager.hasSession(e.player)) {
            SessionManager.updateSession(e.player, handleSettingZone(SessionManager.getSession(e.player)!!, e))
        }
    }

    plugin.listen<PlayerInteractEvent>(BukkitEventPriority.LOWEST) { e ->

        if (SessionManager.hasSession(e.player)) {
            SessionManager.updateSession(e.player, handleSettingZone(SessionManager.getSession(e.player)!!, e))
        }

        PropertyType.DENY_BLOCK_OPERATION.zones.keys.forEach {
            plugin.schedule(true) {
                if (it.has(e.player))
                    e.isCancelled = true
            }
        }
    }

    plugin.listen<PlayerMoveEvent>(BukkitEventPriority.LOWEST) { e ->
        PropertyType.DENY_PLAYER_ENTRY.zones.keys.forEach {
            plugin.schedule(true) {
                if (it.isEntry(e))
                    e.isCancelled = true
            }
        }

        PropertyType.DENY_PLAYER_LEAVE.zones.keys.forEach {
            plugin.schedule(true) {
                if (it.isLeave(e))
                    e.isCancelled = true
            }
        }
    }

    plugin.listen<EntityDamageByEntityEvent>(BukkitEventPriority.LOWEST) { e ->
        PropertyType.DENY_PVP.zones.keys.forEach {
            plugin.schedule(true) {
                if (e.entity is Player && it.has(e.entity as Player) && e.damager is Player && it.has(e.damager as Player))
                    e.isCancelled = true
            }
        }
    }

    plugin.listen<EntityDamageEvent>(BukkitEventPriority.LOWEST) { e ->
        PropertyType.DENY_PLAYER_INJURE.zones.keys.forEach {
            plugin.schedule(true) {
                if (e.entity is Player && it.has(e.entity))
                    e.isCancelled = true
            }
        }
    }
}