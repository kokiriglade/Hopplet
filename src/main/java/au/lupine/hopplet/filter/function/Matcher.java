package au.lupine.hopplet.filter.function;

import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/// A function type that evaluates context on a per-argument basis.
/// Use this if your function requires arguments, but does not require knowing the state of all arguments simultaneously.
///
/// For example, if a user specifies `foo:bar(x, y, z)`:
/// If all of `x`, `y`, and, `z` can be evaluated independent of each other, this is the right choice.
///
/// You can override {@link #strategy()} to change the matching strategy, for example to match all arguments instead of just one.
public interface Matcher<ArgumentType> extends Function<List<ArgumentType>> {

    default @NonNull MatchStrategy<ArgumentType> strategy() {
        return MatchStrategy.any();
    }

    @NonNull ArgumentType parse(@NonNull String argument) throws FilterCompileException;

    boolean matches(@NonNull Context context, @NonNull ArgumentType argument);

    @Override
    default @NonNull List<ArgumentType> compile(@NonNull List<String> arguments) throws FilterCompileException {
        if (arguments.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.default.compilation.exception.no_arguments_provided",
                    Argument.string("name", name())
                )
            );
        }

        List<ArgumentType> args = new ArrayList<>();
        arguments.forEach(arg -> args.add(parse(arg)));
        return args;
    }

    @Override
    default boolean test(@NonNull Context context, @NonNull List<ArgumentType> argument) {
        return strategy().test(context, argument, this::matches);
    }

    @FunctionalInterface
    interface MatchStrategy<ArgumentType> {

        boolean test(
            @NonNull Context context,
            @NonNull List<ArgumentType> arguments,
            @NonNull BiPredicate<Context, ArgumentType> matches
        );

        static <ArgumentType> @NonNull MatchStrategy<ArgumentType> all() {
            return (context, arguments, matches) -> {
                for (ArgumentType argument : arguments) {
                    if (!matches.test(context, argument)) return false;
                }
                return true;
            };
        }

        static <ArgumentType> @NonNull MatchStrategy<ArgumentType> and() {
            return all();
        }

        static <ArgumentType> @NonNull MatchStrategy<ArgumentType> any() {
            return (context, arguments, matches) -> {
                for (ArgumentType argument : arguments) {
                    if (matches.test(context, argument)) return true;
                }
                return false;
            };
        }

        static <ArgumentType> @NonNull MatchStrategy<ArgumentType> or() {
            return any();
        }
    }
}
