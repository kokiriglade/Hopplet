package au.lupine.hopplet.filter.compiler;

import au.lupine.hopplet.event.FunctionTestedEvent;
import au.lupine.hopplet.event.PreFunctionTestEvent;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Function;
import org.jspecify.annotations.NonNull;

import java.util.List;

public sealed interface Node {

    boolean evaluate(@NonNull Context context);

    record Call<ArgumentType>(@NonNull Function<ArgumentType> function, @NonNull ArgumentType argument) implements Node {
        @Override public boolean evaluate(@NonNull Context context) {
            PreFunctionTestEvent<ArgumentType> event = new PreFunctionTestEvent<>(function, context, argument);

            boolean result = function.test(context, argument) && event.callEvent();

            new FunctionTestedEvent<>(function, context, argument, result).callEvent();

            return result;
        }

        static <ArgumentType> @NonNull Call<ArgumentType> of(@NonNull Function<ArgumentType> function, @NonNull List<String> arguments) throws FilterCompileException {
            ArgumentType compiled = function.compile(arguments);
            return new Call<>(function, compiled);
        }
    }

    record And(@NonNull Node left, @NonNull Node right) implements Node {
        @Override public boolean evaluate(@NonNull Context context) {
            return left.evaluate(context) && right.evaluate(context);
        }
    }

    record Or(@NonNull Node left, @NonNull Node right) implements Node {
        @Override public boolean evaluate(@NonNull Context context) {
            return left.evaluate(context) || right.evaluate(context);
        }
    }

    record Not(@NonNull Node inner) implements Node {
        @Override public boolean evaluate(@NonNull Context context) {
            return !inner.evaluate(context);
        }
    }

    record Constant(boolean value) implements Node {
        @Override public boolean evaluate(@NonNull Context context) {
            return value;
        }
    }
}
