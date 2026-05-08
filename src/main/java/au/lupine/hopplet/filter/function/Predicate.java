package au.lupine.hopplet.filter.function;

import au.lupine.hopplet.filter.exception.FilterCompileException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface Predicate extends Function<Void> {

    @Override
    default @NonNull Void compile(@NonNull List<String> arguments) {
        if (!arguments.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.default.compilation.exception.arguments_not_required",
                    Argument.string("name", name())
                )
            );
        }

        return null;
    }
}
