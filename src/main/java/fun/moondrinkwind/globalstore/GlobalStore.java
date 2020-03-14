package fun.moondrinkwind.globalstore;

import fun.moondrinkwind.globalstore.command.CommandRegister;
import fun.moondrinkwind.globalstore.configuration.ConfigurationLoader;
import fun.moondrinkwind.globalstore.util.DatabaseUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public final class GlobalStore extends JavaPlugin {
    private static GlobalStore self = null;
    private static Economy economy;
    private static ConfigurationLoader configurationLoader = null;

    public GlobalStore() {
        saveDefaultConfig();
        saveConfig();
        this.self = this;
        configurationLoader = new ConfigurationLoader();
        configurationLoader.loaderDatabaseConfig();
    }

    public static GlobalStore getSelf() {
        return self;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static ConfigurationLoader getConfigurationLoader() {
        return configurationLoader;
    }

    @Override
    public void onEnable() {
        CommandRegister.registerAllCommands();
        DatabaseUtil.createTable();
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    }

    @Override
    public void onDisable() {
        DatabaseUtil.closeDataSource();
    }
}
