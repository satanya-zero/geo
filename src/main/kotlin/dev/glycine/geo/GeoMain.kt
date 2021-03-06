package dev.glycine.geo

import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import dev.glycine.geo.handlers.handleCommand
import dev.glycine.geo.handlers.registerListeners
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.info
import hazae41.minecraft.kutils.bukkit.init
import hazae41.minecraft.kutils.bukkit.warning
import dev.glycine.geo.managers.CanManager.canCollection
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider

class GeoMain : BukkitPlugin() {

    private lateinit var database: MongoDatabase

    override fun onEnable() {
        init(Config)

        info("============ INFO START ============")
        info(Config.ver)
        info(Config.debug.toString())
        info(Config.host)
        info(Config.port)
        info(Config.db)
        info("============ INFO END ============")

        if (Config.ver != Config.currentVer){
            info("配置文件過時")
            saveConfig()
            reloadConfig()
        }
        //連接MongoDB

        val codec = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )

        try {
            database = MongoClients
                .create(
                    MongoClientSettings
                        .builder()
                        .codecRegistry(codec)
                        .build()
                )
                .getDatabase("test")

            canCollection = database.getCollection("zone", Can::class.java)
            info("&a連接MongoDB數據庫成功")
        } catch (e: MongoException) {
            warning("&cMongoDB連接失敗 \n$e")
        }

        //處理指令
        handleCommand(this)

        //註冊監聽器
        registerListeners(this)
    }

    override fun onDisable() {
        info("Disable GEO succeed!")
    }

    override fun onLoad() {
        val logo = """
                ___           ___           ___     
               /\__\         /\__\         /\  \    
              /:/ _/_       /:/ _/_       /::\  \   
             /:/ /\  \     /:/ /\__\     /:/\:\  \  
            /:/ /::\  \   /:/ /:/ _/_   /:/  \:\  \ 
           /:/__\/\:\__\ /:/_/:/ /\__\ /:/__/ \:\__\
           \:\  \ /:/  / \:\/:/ /:/  / \:\  \ /:/  /
            \:\  /:/  /   \::/_/:/  /   \:\  /:/  / 
             \:\/:/  /     \:\/:/  /     \:\/:/  /  
              \::/  /       \::/  /       \::/  /   
               \/__/         \/__/         \/__/
        """
        info("Welcome using G-E-O!")
        info("&5$logo")
    }
}
