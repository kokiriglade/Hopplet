package au.lupine.hopplet.filter;

import au.lupine.hopplet.event.FilterTestedEvent;
import au.lupine.hopplet.event.PreFilterTestEvent;
import au.lupine.hopplet.filter.compiler.Compiler;
import au.lupine.hopplet.filter.compiler.Node;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.context.ItemStackContext;
import au.lupine.hopplet.filter.edit.EditTarget;
import au.lupine.hopplet.filter.edit.HopperEditTarget;
import au.lupine.hopplet.filter.edit.HopperMinecartEditTarget;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Nameable;
import org.bukkit.block.Hopper;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class Filter {

    /// This style must be present for a filter on a hopper to be valid.
    /// Third-party users of Hopplet's filters can use it, but it's not necessary.
    /// Not using it will break {@link #of(Component)}.
    public static Style style = Style.style(NamedTextColor.GOLD);

    private final @NonNull Node root;
    private final @NonNull String raw;

    public Filter(@NonNull Node root, @NonNull String raw) {
        this.root = root;
        this.raw = raw;
    }

    public @NonNull Node root() {
        return root;
    }

    public @NonNull String raw() {
        return raw;
    }

    public @NonNull Component component() {
        return Component.text(raw, style);
    }

    /// @return `true` if the specified component "looks like" a filter.
    /// This doesn't mean it will successfully compile, but that it has the correct styling.
    public static boolean looksLikeFilter(@NonNull Component component) {
        return component.style().equals(style);
    }

    public static @Nullable Filter of(@NonNull String raw) throws FilterCompileException {
        return Compiler.compile(raw);
    }

    public static @Nullable Filter of(@Nullable Component component, boolean styled) throws FilterCompileException {
        return Compiler.compile(component, styled);
    }

    public static @Nullable Filter of(@Nullable Component component) throws FilterCompileException {
        return Compiler.compile(component);
    }

    public static @Nullable Filter of(@NonNull Nameable nameable) throws FilterCompileException {
        return Compiler.compile(nameable);
    }

    /// @return `true` if the filter accepts the specified {@link Context}.
    public boolean test(@NonNull Context context) {
        PreFilterTestEvent event = new PreFilterTestEvent(this, context);

        boolean result = root.evaluate(context) && event.callEvent();

        new FilterTestedEvent(this, context, result).callEvent();

        return result;
    }

    /// @return `true` if the filter accepts the specified {@link ItemStack}.
    public boolean test(@NonNull ItemStack stack) {
        return test(new ItemStackContext(stack));
    }

    public static void edit(@NonNull EditTarget target, @NonNull Filter filter) {
        target.edit(filter);
    }

    public static void edit(@NonNull Hopper hopper, @NonNull Filter filter) {
        edit(new HopperEditTarget(hopper), filter);
    }

    public static void edit(@NonNull HopperMinecart hopper, @NonNull Filter filter) {
        edit(new HopperMinecartEditTarget(hopper), filter);
    }
}
