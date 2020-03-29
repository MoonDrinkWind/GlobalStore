package fun.moondrinkwind.globalstore.pojo;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class Commodity {
    private int id;
    private ItemStack itemStack;
    private OfflinePlayer player;
    private double price;

    public Commodity(int id, ItemStack itemStack, OfflinePlayer player, double price) {
        this.id = id;
        this.itemStack = itemStack;
        this.player = player;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "id=" + id +
                ", itemStack=" + itemStack +
                ", player=" + player +
                ", price=" + price +
                '}';
    }
}
