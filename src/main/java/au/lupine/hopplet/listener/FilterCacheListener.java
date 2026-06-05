package au.lupine.hopplet.listener;

import au.lupine.hopplet.filter.Filter;
import au.lupine.hopplet.filter.cache.Cache;
import au.lupine.hopplet.filter.compiler.Compiler;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import ca.spottedleaf.concurrentutil.map.concurrent.longs.ConcurrentChainedLong2ReferenceHashTable;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jspecify.annotations.NonNull;

import java.util.Collection;

public final class FilterCacheListener implements Listener {

    private void invalidate(@NonNull World world, @NonNull Collection<Block> blocks) {
        ConcurrentChainedLong2ReferenceHashTable<AbstractInt2ObjectMap<Filter>> worldChunkCache = Cache.BLOCK_CACHE.get(world.getUID());
        if (worldChunkCache == null) return;

        for (Block block : blocks) {
            if (block.getType() != Material.HOPPER) continue;

            AbstractInt2ObjectMap<Filter> chunkCache = worldChunkCache.get(Chunk.getChunkKey(block.getX() >> 4, block.getZ() >> 4));
            if (chunkCache == null) continue;

            chunkCache.remove(Cache.packChunkRelativeCoords(block.getX(), block.getY(), block.getZ()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull BlockPlaceEvent event) {
        if (!(event.getBlock().getState(false) instanceof Hopper hopper)) return;

        Filter filter;
        try {
            filter = Compiler.compile(hopper);
        } catch (FilterCompileException e) {
            return;
        }

        if (filter == null) return;

        Cache.cache(hopper, filter);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.HOPPER) Cache.invalidate(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof HopperMinecart hopper)) return;

        Filter filter;
        try {
            filter = Compiler.compile(hopper);
        } catch (FilterCompileException e) {
            return;
        }

        if (filter == null) return;

        Cache.cache(hopper, filter);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull EntityRemoveFromWorldEvent event) {
        if (event.getEntity() instanceof HopperMinecart hopper) Cache.invalidate(hopper);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull BlockExplodeEvent event) {
        invalidate(event.getBlock().getWorld(), event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull EntityExplodeEvent event) {
        invalidate(event.getEntity().getWorld(), event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(@NonNull WorldUnloadEvent event) {
        Cache.BLOCK_CACHE.remove(event.getWorld().getUID());
    }

    @EventHandler
    public void cleanupBlockFilterCache(@NonNull ChunkUnloadEvent event) {
        ConcurrentChainedLong2ReferenceHashTable<?> worldChunkCache = Cache.BLOCK_CACHE.get(event.getWorld().getUID());

        if (worldChunkCache == null) return;

        Chunk chunk = event.getChunk();
        worldChunkCache.remove(Chunk.getChunkKey(chunk.getX(), chunk.getZ()));
    }
}
