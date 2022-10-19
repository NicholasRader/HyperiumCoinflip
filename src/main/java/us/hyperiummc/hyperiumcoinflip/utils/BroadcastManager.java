package us.hyperiummc.hyperiumcoinflip.utils;

import java.util.HashMap;
import org.bukkit.entity.Player;
import us.hyperiummc.hyperiumcoinflip.HyperiumCoinflip;

public class BroadcastManager {
    private HashMap<Player, Boolean> broadcast = new HashMap<>();

    public boolean inEntry(Player p) {
        return this.broadcast.containsKey(p);
    }

    public void createEntry(Player p) {
        this.broadcast.put(p, Boolean.valueOf(true));
    }

    public void removeEntry(Player p) {
        this.broadcast.remove(p);
    }

    public String toString(Player p) {
        if (inEntry(p))
            return HyperiumCoinflip.getInstance().getConfig().getString("Messages.ToggleON");
        return HyperiumCoinflip.getInstance().getConfig().getString("Messages.ToggleOFF");
    }
}
