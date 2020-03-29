package fun.moondrinkwind.globalstore.listener;

import fun.moondrinkwind.globalstore.GlobalStore;
import fun.moondrinkwind.globalstore.pojo.Commodity;
import fun.moondrinkwind.globalstore.util.DatabaseUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory target = event.getClickedInventory();
        if(GlobalStore.getSelf().getStorePages().containsValue(target)){
            int nowPage = Integer.parseInt(String.valueOf(target.getTitle().indexOf(1)));
            HashMap<Integer, Inventory> storePages = GlobalStore.getSelf().getStorePages();
            Player player = (Player) event.getWhoClicked();
            if(event.getRawSlot() == 46 && nowPage != 1){
                player.closeInventory();
                player.openInventory(storePages.get(nowPage - 1));
            }
            else if(event.getRawSlot() == 52 && nowPage != storePages.size()){
                player.closeInventory();
                player.openInventory(storePages.get(nowPage + 1));
            }
            else{
                ItemStack targetItem = event.getCurrentItem();
                int id = 0;
                for (String lore : targetItem.getItemMeta().getLore()) {
                    if(lore.contains("ID: ")){
                        id = Integer.parseInt(lore.replaceAll("ID: ", ""));
                    }
                }
                Commodity commodity = DatabaseUtil.getItem(id);
                Economy economy = GlobalStore.getEconomy();
                System.out.println(commodity);
                if(economy.has(player, commodity.getPrice())){
                    economy.depositPlayer(player, commodity.getPrice());
                    DatabaseUtil.deleteItem(id);
                    player.getInventory().addItem(commodity.getItemStack());
                    player.sendMessage(ChatColor.GREEN + "购买成功!");
                }
                else{
                    player.sendMessage(ChatColor.DARK_RED + "您没有足够的钱嗷~");
                }
            }
            event.setCancelled(true);
        }
    }
}
