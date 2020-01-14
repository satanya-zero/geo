import enums.ZoneType
import managers.ZoneManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerMoveEvent
import org.locationtech.jts.geom.MultiPolygon
import utils.geometryFrom
import utils.toJTSPoint
import utils.within
import java.util.*

/**
 * @param name 區域的名稱
 * @param uuid 區域唯一id
 * @param founderID 創建者
 * @param worldID 所在的世界
 * @param data 定義區域的地理數據
 * @param type 類型，為空則說明沒有類型
 * @param note 備注，為空則說明沒有備註
 *
 */
data class Zone(
        val name: String,
        val uuid: UUID,
        private val founderID: UUID,
        private val worldID: UUID,
        private val data: String,
        val floor: Double,
        val ceil: Double,
        val type: ZoneType?,
        private val note: String?
) {
    fun getFounder() = Bukkit.getOfflinePlayer(founderID)

    fun getWorld() = Bukkit.getWorld(worldID)

    fun getData() = geometryFrom(data) as MultiPolygon

    fun register() = ZoneManager.register(this)

    fun isEntry(e: PlayerMoveEvent) = !contain(e.from) && contain(e.to ?: e.from)

    fun isLeave(e: PlayerMoveEvent) =  contain(e.from) && !contain(e.to ?: e.from)

    private fun contain(l: Location) = l.within(this) &&  l.y > floor && l.y < ceil

    fun has(e: Entity) = contain(e.location)

    fun getPlayers() = getWorld()?.players?.filter { has(it) }

    fun getEntities() = getWorld()?.entities?.filter { has(it) }
}
