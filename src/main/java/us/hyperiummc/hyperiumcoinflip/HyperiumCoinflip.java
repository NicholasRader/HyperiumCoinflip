package us.hyperiummc.hyperiumcoinflip;

import me.revils.revenchants.api.CurrencyReceiveReason;
import me.revils.revenchants.api.RevEnchantsApi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import us.hyperiummc.hyperiumcoinflip.commands.CoinflipCommand;
import us.hyperiummc.hyperiumcoinflip.events.ClickEvent;
import us.hyperiummc.hyperiumcoinflip.events.PlayerQuitEvent;
import us.hyperiummc.hyperiumcoinflip.utils.*;

import java.util.Map;

public class HyperiumCoinflip extends JavaPlugin {
    private CoinManager coins;

    private StatsManager stats;

    private InventoryManager menu;

    private AnimationManager animation;

    private BroadcastManager broadcast;

    private static Economy econ = null;

//    private File dataFile = new File(getDataFolder(), "data.yml");
//    private FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

    public void onEnable() {
        saveDefaultConfig();
//        if(!dataFile.exists()) {
//            saveResource("data.yml", false);
//        }

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.broadcast = new BroadcastManager();
        this.stats = new StatsManager();
        this.coins = new CoinManager();
        this.menu = new InventoryManager();
        this.animation = new AnimationManager();
        getCommand("coinflip").setExecutor(new CoinflipCommand());
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitEvent(), this);

//        if(dataConfig.contains("data"))
//            restoreFlips();
        this.menu.updateInv();
    }

    public void onDisable() {
//        saveEntries();
        refundPlayers();
    }

    public static HyperiumCoinflip getInstance() {
        return JavaPlugin.getPlugin(HyperiumCoinflip.class);
    }

    public BroadcastManager getBroadcast() {
        return this.broadcast;
    }

    public StatsManager getStats() {
        return this.stats;
    }

    public CoinManager getCoins() {
        return this.coins;
    }

    public Inventory getMenu() {
        return this.menu.getMenu();
    }

    public AnimationManager getAnimation() {
        return this.animation;
    }

    public InventoryManager getMenuManager() {
        return this.menu;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    private void refundPlayers() {
        for(Map.Entry<Player, CoinEntry> entry : coins.getEntry().entrySet()) {
            Player p = entry.getKey();
            double amount = entry.getValue().getAmount();
            boolean currency = entry.getValue().getCurrency();
            if (currency)
                HyperiumCoinflip.getEconomy().depositPlayer(p, amount);
            else
                RevEnchantsApi.depositCurrency(p, (long)amount, "Tokens", CurrencyReceiveReason.CONSOLE);
            this.coins.removeEntry(p);
        }
    }

//    private void saveEntries() {
//        if(coins.getEntry().isEmpty())
//            return;
//
//        for(Map.Entry<Player, CoinEntry> entry : coins.getEntry().entrySet()) {
//            double amount = entry.getValue().getAmount();
//            boolean side = entry.getValue().getSide();
//            boolean currency = entry.getValue().getCurrency();
//            dataConfig.set("data." + entry.getKey().getUniqueId() + ".amount", amount);
//            dataConfig.set("data." + entry.getKey().getUniqueId() + ".side", side);
//            dataConfig.set("data." + entry.getKey().getUniqueId() + ".currency", currency);
//        }
//
//        saveData();
//    }

//    private void restoreFlips() {
//        dataConfig.getConfigurationSection("data").getKeys(false).forEach(key -> {
//            CoinEntry entry = new CoinEntry(dataConfig.getInt("data." + key + ".amount"),
//                    dataConfig.getBoolean("data." + key + ".side"), dataConfig.getBoolean("data." + key + ".currency"));
//            UUID p = UUID.fromString(key);
//            coins.getEntry().put(Bukkit.getOfflinePlayer(p), entry);
//        });
//
//        dataConfig.set("data", null);
//        saveData();
//
//        menu.updateInv();
//    }
//
//    private void saveData() {
//        try {
//            dataConfig.save(dataFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
