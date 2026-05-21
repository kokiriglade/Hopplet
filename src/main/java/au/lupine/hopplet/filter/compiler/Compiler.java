package au.lupine.hopplet.filter.compiler;

import au.lupine.hopplet.filter.Filter;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Nameable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public final class Compiler {

    /// Compiles a raw string into a {@link Filter}.
    /// @return A compiled Filter, or `null` if the specified string is null, empty, or contains only whitespace.
    /// @throws FilterCompileException Thrown if the specified string is non-empty but has a compilation error.
    public static @Nullable Filter compile(@NonNull String raw) throws FilterCompileException {
        if (raw.isBlank()) return null;

        List<Token> tokens = Tokeniser.tokenise(raw);
        Node root = new Parser(tokens).parse();

        return new Filter(root, raw);
    }

    /// Compiles a component into a {@link Filter}.
    /// @return A compiled filter, or null if the component is null, or does not contain {@link Filter#style}.
    /// @throws FilterCompileException Thrown if the underlying string has a compilation error.
    public static @Nullable Filter compile(@Nullable Component component) throws FilterCompileException {
        if (component == null) return null;

        if (!component.style().equals(Filter.style)) return null; // Extract this check for API?

        return compile(PlainTextComponentSerializer.plainText().serialize(component));
    }

    public static @Nullable Filter compile(@NonNull Nameable nameable) throws FilterCompileException {
        return compile(nameable.customName());
    }
}
