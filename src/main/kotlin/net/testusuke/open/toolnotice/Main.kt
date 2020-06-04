package net.testusuke.open.toolnotice

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    companion object {
        lateinit var plugin: Main
        var enabled: Boolean = false
        var prefix: String = "§e[§aTool§6Notice§e]§f"

    }

    val generalPermission = "toolnotice.general"
    val adminPermission = "toolnotice.admin"

    private val pluginName = "ToolNotice"

    override fun onEnable() {
        plugin = this
        //  Logger
        logger.info("==============================")
        logger.info("Plugin: $pluginName")
        logger.info("Author: testusuke")
        logger.info("==============================")
        // config
        this.saveDefaultConfig()
        loadData()
        //  Command
        getCommand("tn")?.setExecutor(NoticeCommand)
        getCommand("toolnotice")?.setExecutor(NoticeCommand)
        //  Event
        server.pluginManager.registerEvents(ToolListener, this)

    }

    override fun onDisable() {

        //  Config
        saveData()
        saveConfig()
    }

    private fun loadData() {
        try {
            enabled = config.getBoolean("mode")
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun saveData() {
        config.set("mode", enabled)
        saveConfig()
    }
}