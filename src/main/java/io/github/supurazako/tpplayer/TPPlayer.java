package io.github.supurazako.tpplayer;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TPPlayer extends JavaPlugin {



    @Override
    public void onEnable() {
        // プラグインが有効になったときに実行される処理
        // コマンドの登録

        PluginCommand tpplayerCommand = getCommand("tpplayer");
        if (tpplayerCommand != null) {
            tpplayerCommand.setExecutor(new TPPlayerCommand(this));
        } else {
            getLogger().warning("Failed to register TPPlayer command. Plugin command is null");
        }
    }

    @Override
    public void onDisable() {
        // プラグインが無効になったときに実行される処理

    }
}
