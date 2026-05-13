package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.context.ItemEntityContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Function;
import au.lupine.hopplet.filter.function.Matcher;
import au.lupine.hopplet.util.Either;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;
import java.util.UUID;

public final class ThrowerFunction implements Matcher<Either<UUID, String>> {

    @Override
    public @NonNull String name() {
        return "thrower";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("thrown_by");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.thrower.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull Either<UUID, String> parse(@NonNull String argument) throws FilterCompileException {
        return Function.playerEither(argument);
    }

    @Override
    public boolean matches(@NonNull Context context, @NonNull Either<UUID, String> either) {
        if (!(context instanceof ItemEntityContext ctx)) return false;

        UUID thrower = ctx.item().getThrower();
        if (thrower == null) return false;

        return either.map(thrower::equals, name -> {
            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
            return player != null && thrower.equals(player.getUniqueId());
        });
    }
}
