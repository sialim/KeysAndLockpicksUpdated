package me.sialim.keysandlockpicks.api.events;

import java.util.ArrayList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MinigameClickEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    private ArrayList<Integer> Sequnze;
    private int correctSlot;
    private InventoryClickEvent inventoryClickEvent;

    public MinigameClickEvent(InventoryClickEvent clickEvent, int correctSlot, ArrayList<Integer> Sequnze) {
        this.setInventoryClickEvent(clickEvent);
        this.setSequnze(Sequnze);
        this.setCorrectSlot(correctSlot);
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

    public ArrayList<Integer> getSequnze() {
        return this.Sequnze;
    }

    public void setSequnze(ArrayList<Integer> sequnze) {
        this.Sequnze = sequnze;
    }

    public InventoryClickEvent getInventoryClickEvent() {
        return this.inventoryClickEvent;
    }

    public void setInventoryClickEvent(InventoryClickEvent inventoryClickEvent) {
        this.inventoryClickEvent = inventoryClickEvent;
    }

    public int getCorrectSlot() {
        return this.correctSlot;
    }

    public void setCorrectSlot(int correctSlot) {
        this.correctSlot = correctSlot;
    }
}
