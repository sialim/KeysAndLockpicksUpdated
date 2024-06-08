package me.sialim.keysandlockpicks.api.events;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class OpenMinigameEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    private Player player;
    private int tier;
    private Inventory inv;
    private ArrayList<Integer> Sequnze;

    public OpenMinigameEvent(Player player, int tier, Inventory inv, ArrayList<Integer> Sequnze) {
        this.setPlayer(player);
        this.setTier(tier);
        this.setInv(inv);
        this.setSequnze(Sequnze);
        this.isCancelled = false;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean arg0) {
        this.isCancelled = arg0;
    }

    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public int getTier() {
        return this.tier;
    }

    private void setTier(int tier) {
        if (tier < -1 && tier > 5) {
            throw new IllegalArgumentException("tier must be between -1 and 5");
        } else {
            this.tier = tier;
        }
    }

    public Inventory getInv() {
        return this.inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public ArrayList<Integer> getSequnze() {
        return this.Sequnze;
    }

    public void setSequnze(ArrayList<Integer> sequnze) {
        this.Sequnze = sequnze;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
