package fun.moondrinkwind.globalstore.configuration;

import fun.moondrinkwind.globalstore.GlobalStore;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationLoader {
    private String databaseURL = null;
    private String databaseUsername = null;
    private String databasePassword = null;
    private String databasePrefix = null;

    public String getDatabasePrefix() {
        return databasePrefix;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void loaderDatabaseConfig(){
        ConfigurationSection databaseConfig = GlobalStore.getSelf().getConfig().getConfigurationSection("database");
        databaseURL = "jdbc:mysql://" + databaseConfig.getString("url") + "/" + databaseConfig.getString("database") + "?serverTimezone=UTC";
        databasePassword = databaseConfig.getString("password");
        databaseUsername = databaseConfig.getString("username");
        databasePrefix = databaseConfig.getString("prefix");
    }
}
