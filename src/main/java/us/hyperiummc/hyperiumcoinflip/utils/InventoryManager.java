package us.hyperiummc.hyperiumcoinflip.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import us.hyperiummc.hyperiumcoinflip.HyperiumCoinflip;

public class InventoryManager {
    private FileConfiguration config = HyperiumCoinflip.getInstance().getConfig();

    private int size = this.config.getInt("GUI.Size");

    private String title = this.config.getString("GUI.Title");

    private Material material;

    private Inventory menu;

    private HashMap<Player, CoinEntry> coins = HyperiumCoinflip.getInstance().getCoins().getEntry();

    public InventoryManager() {
        this.menu = Bukkit.createInventory(null, this.size, Chat.color(this.title));
        for (int i = 0; i < this.size; i++) {
            this.menu.setItem(i, crateGlass(1, 15));
            this.menu.setItem(this.size - 2, playerHelp());
            this.menu.setItem(this.size - 1, playerRefresh());
        }
    }

    public Inventory getMenu() {
        return this.menu;
    }

    public int getSize() { return this.size; }

    public void updateInv() {
        int index = 0;
        this.menu = Bukkit.createInventory(null, this.size, Chat.color(this.title));
        for (int i = 0; i < this.size - 2; i++)
            this.menu.setItem(i, crateGlass(1, 15));
        for (Player p : this.coins.keySet()) {
            this.menu.setItem(index, playerBet(p));
            index++;
        }
        this.menu.setItem(this.size - 2, playerHelp());
        this.menu.setItem(this.size - 1, playerRefresh());
    }

    public ItemStack playerBet(Player p) {
        DecimalFormat df = new DecimalFormat("#,###");
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        List<String> desc = this.config.getStringList("GUI.SkullItem.lore");
        List<String> lore = new ArrayList<>();
        String currencySymbol;
        if(this.coins.get(p).getCurrency())
            currencySymbol = "&2\\$&a";
        else
            currencySymbol = "&e⛀";
        for (String text : desc)
            lore.add(Chat.color(text.replaceAll("%currency%", currencySymbol).replaceAll("%money%", df.format(this.coins.get(p).getAmount()))
                    .replaceAll("%side%", HyperiumCoinflip.getInstance().getCoins().getSideConverted(p))));
        meta.setDisplayName(Chat.color(this.config.getString("GUI.SkullItem.name").replaceAll("%name%", p.getName())));
        meta.setOwningPlayer(p);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack playerHelp() {
        String type = this.config.getString("GUI.BookInfo.type");
        this.material = Material.getMaterial(type);
        ItemStack item = new ItemStack(this.material, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> desc = this.config.getStringList("GUI.BookInfo.lore");
        List<String> lore = new ArrayList<>();
        for (String text : desc)
            lore.add(Chat.color(text));
        meta.setDisplayName(Chat.color(this.config.getString("GUI.BookInfo.name")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack playerRefresh() {
        String type = this.config.getString("GUI.RefreshItem.type");
        this.material = Material.getMaterial(type);
        ItemStack item = new ItemStack(this.material, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> desc = this.config.getStringList("GUI.RefreshItem.lore");
        List<String> lore = new ArrayList<>();
        for (String text : desc)
            lore.add(Chat.color(text));
        meta.setDisplayName(Chat.color(this.config.getString("GUI.RefreshItem.name")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack crateGlass(int amount, int data) {
        ItemStack glass = new ItemStack(Material.LEGACY_STAINED_GLASS_PANE, amount, (byte)data);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(Chat.color("&0."));
        glass.setItemMeta(meta);
        return glass;
    }
}
