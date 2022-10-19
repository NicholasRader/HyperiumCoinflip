package us.hyperiummc.hyperiumcoinflip.events;

import me.revils.revenchants.api.CurrencyReceiveReason;
import me.revils.revenchants.api.RevEnchantsApi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.hyperiummc.hyperiumcoinflip.HyperiumCoinflip;
import us.hyperiummc.hyperiumcoinflip.utils.Chat;
import us.hyperiummc.hyperiumcoinflip.utils.CoinManager;
import us.hyperiummc.hyperiumcoinflip.utils.InventoryManager;

public class ClickEvent implements Listener {

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        Inventory open = e.getInventory();
        ItemStack item = e.getCurrentItem();
        Economy econ = HyperiumCoinflip.getEconomy();
        CoinManager coins = HyperiumCoinflip.getInstance().getCoins();
        InventoryManager menu = HyperiumCoinflip.getInstance().getMenuManager();
        if(!open.equals(menu.getMenu()))
            return;
        if (open.getHolder() == HyperiumCoinflip.getInstance().getMenu().getHolder() ||
                e.getView().getTitle().equals(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("AnimationGUI.Title")))) {
            e.setCancelled(true);
            if (item == null || !item.hasItemMeta())
                return;
            if (item.equals(menu.playerRefresh())) {
                p.closeInventory();
                p.openInventory(menu.getMenu());
                return;
            }
            Player other = Bukkit.getServer().getPlayer(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
            if (item.equals(menu.playerHelp()))
                return;
            if (coins.getEntry().containsKey(p))
                return;
            if (item.getItemMeta().getDisplayName().equals(Chat.color("&0.")))
                return;
            if (item.getType().equals(Material.PLAYER_HEAD)) {
                if (coins.inEntry(other)) {
                    double amount = coins.getEntry().get(other).getAmount();
                    boolean currency = coins.getEntry().get(other).getCurrency();
                    if(currency) {
                        if (econ.getBalance(p) >= amount) {
                            econ.withdrawPlayer(p, amount);
                            p.closeInventory();
                            coins.removeEntry(other);
                            Player winner = HyperiumCoinflip.getInstance().getStats().getWinner(p, other);
                            amount *= 2.0D;
                            if (p.equals(winner)) {
                                HyperiumCoinflip.getInstance().getAnimation().setAnimation(p, p, other, amount, currency);
                            } else {
                                HyperiumCoinflip.getInstance().getAnimation().setAnimation(p, other, p, amount, currency);
                            }
                            menu.updateInv();
                        } else {
                            p.closeInventory();
                            p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
                        }
                    }
                    else {
                        if (RevEnchantsApi.getCurrency(p, "Tokens") >= amount) {
                            RevEnchantsApi.withdrawCurrency(p, (long)amount, "Tokens");
                            p.closeInventory();
                            coins.removeEntry(other);
                            Player winner = HyperiumCoinflip.getInstance().getStats().getWinner(p, other);
                            amount *= 2.0D;
                            if (p.equals(winner)) {
                                HyperiumCoinflip.getInstance().getAnimation().setAnimation(p, p, other, amount, currency);
                            } else {
                                HyperiumCoinflip.getInstance().getAnimation().setAnimation(p, other, p, amount, currency);
                            }
                            menu.updateInv();
                        } else {
                            p.closeInventory();
                            p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
                        }
                    }
                }
            } else if (item.getItemMeta().getDisplayName().equals(Chat.color("&b&l" + other.getName()))) {
                return;
            }
        }
    }

}
