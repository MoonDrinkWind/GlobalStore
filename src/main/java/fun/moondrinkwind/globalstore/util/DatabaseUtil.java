package fun.moondrinkwind.globalstore.util;

import cc.zoyn.core.util.serializer.ItemSerializerUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fun.moondrinkwind.globalstore.GlobalStore;
import fun.moondrinkwind.globalstore.configuration.ConfigurationLoader;
import fun.moondrinkwind.globalstore.pojo.Commodity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseUtil {
    private final static String DIVER = "com.mysql.cj.jdbc.Driver";
    private static HikariDataSource dataSource = null;
    private static ConfigurationLoader configurationLoader = GlobalStore.getConfigurationLoader();
    private static String tableName = configurationLoader.getDatabasePrefix() + "COMMODITY";

    public static void createTable(){
        String sql = "CREATE TABLE " + tableName +"(ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT, ITEM VARCHAR(256) NOT NULL, PRICE DOUBLE NOT NULL, PLAYER VARCHAR(64) NOT NULL)";
        if(!tableExist(tableName)){
            try {
                Connection connection = getConnection();
                connection.createStatement().execute(sql);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertItem(ItemStack item, double price, Player player){
        String itemBase64 = ItemSerializerUtils.toBase64(new ItemStack[]{item});
        String ownerUUID = player.getUniqueId().toString();
        String sql = "INSERT INTO " + configurationLoader.getDatabasePrefix() + "COMMODITY(ITEM, PRICE, PLAYER) VALUES (?, ?, ?)";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, itemBase64);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, ownerUUID);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(int id){
        String sql = "DELETE FROM " + configurationLoader.getDatabasePrefix() + "COMMODITY " + "WHERE ID = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Commodity getItem(int id){
        Commodity target = null;
        String sql = "SELECT ID, ITEM, PRICE, PLAYER FROM " + tableName + " WHERE ID = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                target = new Commodity(resultSet.getInt("ID"),
                        ItemSerializerUtils.fromBase64(resultSet.getString("ITEM"))[0],
                        Bukkit.getPlayer(UUID.fromString(resultSet.getString("OWNER"))),
                        resultSet.getDouble("PRICE"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return target;
    }

    public static List<Commodity> getAllItems(){
        List<Commodity> commodities = new ArrayList<>();
        try{
            Connection connection = getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT ID, ITEM, PRICE, PLAYER FROM " + tableName);
            while (resultSet.next()){
                commodities.add(new Commodity(resultSet.getInt("ID"),
                        ItemSerializerUtils.fromBase64(resultSet.getString("ITEM"))[0],
                        Bukkit.getPlayer(UUID.fromString(resultSet.getString("PLAYER"))),
                        resultSet.getDouble("PRICE")));
            }
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return commodities;
    }

    private static Connection getConnection() throws SQLException {
        if (dataSource == null){
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(DIVER);
            config.setJdbcUrl(configurationLoader.getDatabaseURL());
            config.setUsername(configurationLoader.getDatabaseUsername());
            config.setPassword(configurationLoader.getDatabasePassword());
            dataSource = new HikariDataSource(config);
        }
        return dataSource.getConnection();
    }

    private static boolean tableExist(String table){
        boolean exist = false;
        try {
            Connection connection = getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            exist = databaseMetaData.getTables(null, null, table, new String[]{"TABLE"}).next();
            connection.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return exist;
    }

    public static void closeDataSource(){
        dataSource.close();
    }
}
