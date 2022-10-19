package us.hyperiummc.hyperiumcoinflip.utils;

import java.text.DecimalFormat;

import me.revils.revenchants.api.CurrencyReceiveReason;
import me.revils.revenchants.api.RevEnchantsApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import us.hyperiummc.hyperiumcoinflip.HyperiumCoinflip;

public class AnimationManager {
    private FileConfiguration config = HyperiumCoinflip.getInstance().getConfig();

    private String title = this.config.getString("AnimationGUI.Title");

    public ItemStack players(Player winner) {
        ItemStack p1 = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setDisplayName(Chat.color("&b&l") + winner.getName());
        meta.setOwningPlayer(winner);
        p1.setItemMeta(meta);
        return p1;
    }

    public void fillGlass(int j, Inventory animation) {
        for (int i = 0; i < 27; i++)
            animation.setItem(i, HyperiumCoinflip.getInstance().getMenuManager().crateGlass(1, j));
    }

    public void setAnimation(final Player p, final Player winner, final Player loser, final double amount, final boolean currency) {
        final DecimalFormat df = new DecimalFormat("#,###");
        final Inventory animation = Bukkit.createInventory(null, 27, Chat.color(this.title));
        p.openInventory(animation);
        (new BukkitRunnable() {
            int counter = 0;

            int counter2 = 1;

            int counter3 = 1;

            public void run() {
                AnimationManager.this.fillGlass(this.counter, animation);
                p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
                if (this.counter % this.counter2 == 0) {
                    this.counter = 0;
                    this.counter2++;
                }
                if (this.counter3 == 1) {
                    animation.setItem(13, AnimationManager.this.players(loser));
                    p.updateInventory();
                    this.counter3--;
                } else {
                    animation.setItem(13, AnimationManager.this.players(winner));
                    p.updateInventory();
                    this.counter3++;
                }
                this.counter++;
                if (this.counter2 > 6) {
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    AnimationManager.this.fillGlass(15, animation);
                    animation.setItem(13, AnimationManager.this.players(winner));
                    p.updateInventory();
                    String currencySymbol;
                    if(currency) {
                        HyperiumCoinflip.getEconomy().depositPlayer(winner, amount);
                        currencySymbol = "&2\\$&a";
                    }
                    else {
                        RevEnchantsApi.depositCurrency(winner, (long)amount, "Tokens", CurrencyReceiveReason.CONSOLE);
                        currencySymbol = "&e⛀";
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!HyperiumCoinflip.getInstance().getBroadcast().inEntry(p))
                            p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.WinBroadcast").replaceAll("%winner%", winner.getName())
                                    .replaceAll("%loser%", loser.getName()).replaceAll("%currency%", currencySymbol)
                                    .replaceAll("%amount%", df.format(amount))));
                    }
                    cancel();
                }
            }
        }).runTaskTimer(HyperiumCoinflip.getInstance(), 0L, 5L);
    }
}
