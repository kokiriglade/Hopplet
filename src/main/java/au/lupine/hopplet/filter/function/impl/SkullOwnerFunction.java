package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Function;
import au.lupine.hopplet.filter.function.Matcher;
import au.lupine.hopplet.util.Either;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;
import java.util.UUID;

public final class SkullOwnerFunction implements Matcher<Either<UUID, String>> {

    @Override
    public @NonNull String name() {
        return "skull_owner";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("head_owner");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.skull_owner.description");
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
        if (!(context.stack().getItemMeta() instanceof SkullMeta meta)) return false;

        PlayerProfile profile = meta.getPlayerProfile();
        if (profile == null) return false;

        UUID profileUUID = profile.getId();
        String profileName = profile.getName();
        if (profileUUID == null && profileName == null) return false;

        return either.map(
            uuid -> uuid.equals(profileUUID),
            name -> {
                if (name.equals(profileName)) return true;

                if (profileUUID != null) {
                    OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
                    return player != null && profileUUID.equals(player.getUniqueId());
                }

                return false;
            }
        );
    }
}
