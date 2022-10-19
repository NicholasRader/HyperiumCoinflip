package us.hyperiummc.hyperiumcoinflip.commands;

import java.text.DecimalFormat;

import me.revils.revenchants.api.CurrencyReceiveReason;
import me.revils.revenchants.api.RevEnchantsApi;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.hyperiummc.hyperiumcoinflip.HyperiumCoinflip;
import us.hyperiummc.hyperiumcoinflip.utils.Chat;
import us.hyperiummc.hyperiumcoinflip.utils.CoinManager;
import us.hyperiummc.hyperiumcoinflip.utils.InventoryManager;

public class CoinflipCommand implements CommandExecutor {
    private CoinManager coins = HyperiumCoinflip.getInstance().getCoins();

    private InventoryManager menu = HyperiumCoinflip.getInstance().getMenuManager();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        DecimalFormat df = new DecimalFormat("#,###");
        if (args.length == 0) {
            p.openInventory(HyperiumCoinflip.getInstance().getMenu());
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.CreateHelp")));
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                if (!HyperiumCoinflip.getInstance().getBroadcast().inEntry(p)) {
                    HyperiumCoinflip.getInstance().getBroadcast().createEntry(p);
                    p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getBroadcast().toString(p)));
                    return true;
                }
                HyperiumCoinflip.getInstance().getBroadcast().removeEntry(p);
                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getBroadcast().toString(p)));
                return true;
            }
            if (args[0].equalsIgnoreCase("cancel")) {
                if (this.coins.inEntry(p)) {
                    double amount = (this.coins.getEntry().get(p)).getAmount();
                    boolean currency = (this.coins.getEntry().get(p)).getCurrency();
                    if(currency)
                        HyperiumCoinflip.getEconomy().depositPlayer(p, amount);
                    else
                        RevEnchantsApi.depositCurrency(p, (long)amount, "Tokens", CurrencyReceiveReason.CONSOLE);
                    this.coins.removeEntry(p);
                    this.menu.updateInv();
                    p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.Canceled")));
                    return true;
                }
                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.NotInBet")));
            } else {
                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.CanceledHelp")));
                return true;
            }
        }
        if (args.length >= 2)
            if(args.length > 3) {
                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.CreateHelp")));
                return true;
            }
            try {
                double amount = Double.parseDouble(args[0]);
                boolean side = this.coins.getBooleanConverted(args[1]);
                boolean currency = true;
                if(args.length == 3) {
                    if(args[2].equalsIgnoreCase("tokens") || args[2].equalsIgnoreCase("token"))
                        currency = false;
                    else {
                        p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.CreateHelp")));
                        return true;
                    }
                }
                if (this.coins.getEntry().size() < this.menu.getSize())
                    if (!this.coins.inEntry(p)) {
                        String currencySymbol;
                        if(currency) {
                            currencySymbol = "&2\\$&a";
                            if (HyperiumCoinflip.getEconomy().getBalance(p) >= amount) {
                                if (amount >= HyperiumCoinflip.getInstance().getConfig().getInt("minAmount")) {
                                    HyperiumCoinflip.getEconomy().withdrawPlayer(p, amount);
                                    p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.Entered").replaceAll("%currency%", currencySymbol).replaceAll("%amount%", df.format(amount))));
                                    if (!HyperiumCoinflip.getInstance().getStats().inEntry(p))
                                        HyperiumCoinflip.getInstance().getStats().createEntry(p);
                                    this.coins.createEntry(p, amount, side, currency);
                                    this.menu.updateInv();
                                    return true;
                                }
                                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.NotEnoughEnterMoney")));
                            } else {
                                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
                            }
                        }
                        else {
                            currencySymbol = "&e⛀";
                            if (RevEnchantsApi.getCurrency(p, "Tokens") >= amount) {
                                if (amount >= HyperiumCoinflip.getInstance().getConfig().getInt("minAmount")) {
                                    RevEnchantsApi.withdrawCurrency(p, (long)amount, "Tokens");
                                    p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.Entered").replaceAll("%currency%", currencySymbol).replaceAll("%amount%", df.format(amount))));
                                    if (!HyperiumCoinflip.getInstance().getStats().inEntry(p))
                                        HyperiumCoinflip.getInstance().getStats().createEntry(p);
                                    this.coins.createEntry(p, amount, side, currency);
                                    this.menu.updateInv();
                                    return true;
                                }
                                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.NotEnoughEnterMoney")));
                            } else {
                                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
                            }
                        }
                    } else {
                        p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.AlreadyInBet")));
                    }
                else {
                    p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.Full")));
                }
            } catch (NumberFormatException e) {
                p.sendMessage(Chat.color(HyperiumCoinflip.getInstance().getConfig().getString("Messages.CreateHelp")));
            }
        return true;
    }
}