package ru.krikunik.noboom;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoBoom extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("NoBoom enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NoBoom disabled!");
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {

        Entity entity = event.getEntity();

        if (!(entity instanceof TNTPrimed)) return;

        RegionManager regionManager = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(entity.getWorld()));

        if (regionManager == null) return;

        ApplicableRegionSet regions = regionManager.getApplicableRegions(
                BukkitAdapter.asBlockVector(entity.getLocation())
        );

        // Логика как в StickHWTnT
        if (!regions.testState(null, Flags.OTHER_EXPLOSION)) {

            // TNT взрывается, но блоки не ломает
            event.blockList().clear();
        }
    }
}
