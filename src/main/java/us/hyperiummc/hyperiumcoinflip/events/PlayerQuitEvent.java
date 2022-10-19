package us.hyperiummc.hyperiumcoinflip.events;

import me.revils.revenchants.api.CurrencyReceiveReason;
import me.revils.revenchants.api.RevEnchantsApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.hyperiummc.hyperiumcoinflip.HyperiumCoinflip;

public class PlayerQuitEvent implements Listener {
    @EventHandler
    public void onQuitEvent(org.bukkit.event.player.PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (HyperiumCoinflip.getInstance().getCoins().inEntry(p)) {
            if(HyperiumCoinflip.getInstance().getCoins().getEntry().get(p).getCurrency())
                HyperiumCoinflip.getEconomy().depositPlayer(p, HyperiumCoinflip.getInstance().getCoins().getEntry().get(p).getAmount());
            else
                RevEnchantsApi.depositCurrency(p, (long)HyperiumCoinflip.getInstance().getCoins().getEntry().get(p).getAmount(), "Tokens", CurrencyReceiveReason.CONSOLE);
            HyperiumCoinflip.getInstance().getCoins().removeEntry(p);
            HyperiumCoinflip.getInstance().getMenuManager().updateInv();
        }
    }
}
