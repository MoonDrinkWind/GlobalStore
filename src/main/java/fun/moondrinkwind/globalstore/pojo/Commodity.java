package fun.moondrinkwind.globalstore.pojo;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commodity {
    private int id;
    private ItemStack itemStack;
    private Player player;
    private double price;

    public Commodity(int id, ItemStack itemStack, Player player, double price) {
        this.id = id;
        this.itemStack = itemStack;
        this.player = player;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    public double getPrice() {
        return price;
    }
}
