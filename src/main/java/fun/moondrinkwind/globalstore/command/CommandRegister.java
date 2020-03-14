package fun.moondrinkwind.globalstore.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;

import java.util.ArrayList;
import java.util.List;

public class CommandRegister {
    public static void registerAllCommands(){
        List<String> globalStoreAlias = new ArrayList<>();
        globalStoreAlias.add("gs");
        GlobalStoreCommand globalStoreCommand = new GlobalStoreCommand("globalStore", "GlobalStore Plugin Main Command", "/<Command>", globalStoreAlias);
        CommandMap commandMap = ((CraftServer)Bukkit.getServer()).getCommandMap();
        commandMap.register("GlobalStore", globalStoreCommand);
    }
}
