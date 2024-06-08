package me.sialim.keysandlockpicks.traps;

import me.sialim.keysandlockpicks.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class RedstoneTrap {
    public static void redstoneTrap(Block block) {
        final Block redBlock = block.getRelative(BlockFace.DOWN);
        final Material redType = redBlock.getType();
        redBlock.setType(Material.REDSTONE_BLOCK);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            public void run() {
                redBlock.setType(redType);
            }
        }, 2L);
    }
}
