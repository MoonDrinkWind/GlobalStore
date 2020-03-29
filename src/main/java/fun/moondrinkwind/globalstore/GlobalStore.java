package fun.moondrinkwind.globalstore;

import cc.zoyn.core.builder.ItemStackBuilder;
import fun.moondrinkwind.globalstore.command.CommandRegister;
import fun.moondrinkwind.globalstore.configuration.ConfigurationLoader;
import fun.moondrinkwind.globalstore.listener.InventoryClickListener;
import fun.moondrinkwind.globalstore.pojo.Commodity;
import fun.moondrinkwind.globalstore.util.DatabaseUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class GlobalStore extends JavaPlugin {
    private static GlobalStore self = null;
    private static Economy economy;
    private static ConfigurationLoader configurationLoader = null;
    private HashMap<Integer, Inventory> storePages = null;

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

    public HashMap<Integer, Inventory> getStorePages() {
        return storePages;
    }

    @Override
    public void onEnable() {
        CommandRegister.registerAllCommands();
        DatabaseUtil.createTable();
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        initStorePage();
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
    }

    @Override
    public void onDisable() {
        DatabaseUtil.closeDataSource();
    }

    public void initStorePage() {
        storePages = new HashMap<>();
        List<Commodity> commodities = DatabaseUtil.getAllItems();
        int count = commodities.size() % 52 == 0 ? (commodities.size() % 52) : (commodities.size() % 52) + 1;
        ItemStack nextPage = new ItemStackBuilder(Material.BARRIER).setDisplayName(ChatColor.GREEN + "下一页").build();
        ItemStack lastPage = new ItemStackBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "上一页").build();
        int indexCommodity = 0;
        for (int i = 1; i <= count; i++) {
            Inventory page = Bukkit.createInventory(null, 54, String.format("第%s页", i));
            page.setItem(46, lastPage);
            page.setItem(52, nextPage);
            while (!(page.firstEmpty() == -1)) {
                if(indexCommodity < commodities.size()){
                    Commodity commodity = commodities.get(indexCommodity);
                    ItemStack itemStack = commodity.getItemStack();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
                    lore.add("ID: " + commodity.getId());
                    lore.add("卖家: " + commodity.getPlayer().getName());
                    lore.add("价格: " + commodity.getPrice());
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);
                    page.addItem(itemStack);
                    indexCommodity ++;
                }

                if(indexCommodity == commodities.size()){
                    break;
                }
            }
            storePages.put(i, page);
        }
    }
}
