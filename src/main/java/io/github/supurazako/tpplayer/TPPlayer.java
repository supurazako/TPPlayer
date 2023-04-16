package io.github.supurazako.tpplayer;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


import java.io.File;
import java.io.IOException;
public final class TPPlayer extends JavaPlugin {


    private static final String CONFIG_FILE_NAME = "config.yml";
    private FileConfiguration config;


    @Override
    public void onEnable() {
        // プラグインが有効になったときに実行される処理
        // コマンドの登録

        PluginCommand tpplayerCommand = getCommand("tpplayer");
        if (tpplayerCommand != null) {
            tpplayerCommand.setExecutor(new TPPlayerCommand());
        } else {
            getLogger().warning("Failed to register TPPlayer command. Plugin command is null");
        }
        loadConfig();
    }

    @Override
    public void onDisable() {
        // プラグインが無効になったときに実行される処理
        saveConfig();
    }


    // コンフィグファイルの読み込みメソッド
    private void loadConfig() {
        // config.yml ファイルのパスを取得
        File configFile = new File(getDataFolder(), CONFIG_FILE_NAME);


        // config フィールドが null の場合に初期化
        if (config == null) {
            config = new YamlConfiguration();
        }


        // config.yml ファイルが存在するか確認し、存在したら読み込む
        if (configFile.exists()) {
            config = YamlConfiguration.loadConfiguration(configFile);
            getLogger().info("Loaded config.yml.");
        } else {
            getLogger().info("config.yml does not exist. Generating default configuration.");
            // config フィールドにデフォルトの設定を生成し、保存する
            config = new YamlConfiguration();
            config.set("permissions.tpplayer\\.tp.description", "Allows the use of tpplayer command");
            config.set("permissions.tpplayer\\.tp.default", true);


            try {
                config.save(configFile);
                getLogger().info("saved config.yml.");
            } catch (IOException e) {
                getLogger().warning("Failed to save config.yml.");
                e.printStackTrace();
            }
        }
    }


    // コンフィグファイルの保存を行うメソッド
    public void saveConfig() {
        // config.yml ファイルのパスを取得
        File configFile = new File(getDataFolder(), CONFIG_FILE_NAME);


        // コンフィグファイルの保存を実行
        try {
            config.save(configFile);
            getLogger().info("saved config.yml.");
        } catch (IOException e) {
            getLogger().warning("Failed to save config.yml.");
            e.printStackTrace();
        }
    }
}
