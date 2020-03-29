package fun.moondrinkwind.globalstore.command;

import fun.moondrinkwind.globalstore.GlobalStore;
import fun.moondrinkwind.globalstore.util.DatabaseUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.regex.Pattern;

public class GlobalStoreCommand extends Command {
    protected GlobalStoreCommand(String name) {
        super(name);
    }

    protected GlobalStoreCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "你不可以使用这个命令，因为你不是一个玩家!");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            sender.sendMessage(ChatColor.GREEN + "/globalStore 价格 将手中的物品商家到全球市场!");
            sender.sendMessage(ChatColor.GREEN + "/globalStore open 打开全球市场!");
            sender.sendMessage(ChatColor.GREEN + "/globalStore mine 查看自己正在出售的所有物品!");
            sender.sendMessage(ChatColor.GOLD + "FAQ");
            sender.sendMessage(ChatColor.RED + "Q: 如何下架自己的物品");
            sender.sendMessage(ChatColor.RED + "A: 打开全球商店-我的商品-点击需要下架的商品 即可下架");
        }
        else if (args.length == 1 && Pattern.compile("\\d+").matcher(args[0]).matches()){
            if(!(player.getInventory().getItemInMainHand().getType() == Material.AIR)){
                double price = Double.parseDouble(args[0]);
                DatabaseUtil.insertItem(player.getInventory().getItemInMainHand(), price, player);
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                player.sendMessage(ChatColor.GOLD + "正在上架!");
                GlobalStore.getSelf().initStorePage();
                player.sendMessage(ChatColor.GREEN + "上架成功!");
            }
            else{
                player.sendMessage(ChatColor.DARK_RED + "手上没有物品哦!");
            }
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("open")){
            if(GlobalStore.getSelf().getStorePages().isEmpty()){
                player.sendMessage(ChatColor.GREEN + "暂时没有物品出售哦!");
            }
            else{
                player.openInventory(GlobalStore.getSelf().getStorePages().get(1));
            }
        }
        else{
            sender.sendMessage(ChatColor.DARK_RED + "你使用的姿势不对,请检查命令或使用/globalStore help获取帮助");
        }
        return true;
    }
}
