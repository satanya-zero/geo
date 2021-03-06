package dev.glycine.geo.handlers

import dev.glycine.geo.GeoMain
import dev.glycine.geo.Can
import hazae41.minecraft.kutils.bukkit.command
import dev.glycine.geo.managers.CanManager
import dev.glycine.geo.managers.SessionManager
import dev.glycine.geo.utils.msg
import org.bukkit.entity.Player

fun handleCommand(plugin: GeoMain){
    /**
     * 註冊主指令
     */
    plugin.command("geo") { sender, args ->
        when {
            args.isEmpty() -> sender.msg("see usage for input /geo ?")

            else -> when (args[0]) {
                //to list some geometry
                "list" -> {
                    if (args.size < 2) sender.msg("see usage for input /geo ?")
                    else when (args[1]) {
                        "can" -> CanManager.canSet.forEach { sender.msg(" ${it.name}  ${it.getWorld()?.name}  ${it.type}  ${it.getFounder().name}") }
                    }
                }

                "tp" -> {

                }

                "build" -> {
                    when {
                        sender !is Player -> sender.msg("You must be a player")
                        args.size < 2 -> sender.msg("see usage for input /geo ?")
                        else -> when (args[1]) {
                            "can" -> {
                                sender.msg("Please input the name of the CAN that you will found: ")
                                SessionManager.newSession<Can>(sender)
                            }
                        }
                    }
                }

                "done" -> {
                    if (sender !is Player) sender.msg("You must be a player")
                    else if (SessionManager.hasSession(sender)) {
                        val builder = SessionManager.getSession(sender)!!
                        if (builder.isDone()) {
                            builder.build().register()
                            SessionManager.finishSession(sender)
                        } else {
                            sender.msg("你尚有build未完成！！ 使用/build clear清空未完成的內容")
                        }
                    }else {
                        sender.msg("你沒有待構建的build，快用/geo build創建一個吧！")
                    }
                }
            }
        }
    }
}