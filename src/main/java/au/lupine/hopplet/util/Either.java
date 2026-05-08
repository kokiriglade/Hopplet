package au.lupine.hopplet.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "OptionalAssignedToNull"})
public abstract class Either<L, R> {

    public abstract <T> T map(@NonNull Function<? super L, ? extends T> left, @NonNull Function<? super R, ? extends T> right);

    public abstract @NonNull Optional<L> left();

    public abstract @NonNull Optional<R> right();

    public static <L, R> @NonNull Either<L, R> left(@NonNull L value) {
        return new Either.Left<>(value);
    }

    public static <L, R> @NonNull Either<L, R> right(@NonNull R value) {
        return new Either.Right<>(value);
    }

    private static final class Left<L, R> extends Either<L, R> {

        private final @NonNull L value;
        private @Nullable Optional<L> optional;

        private Left(@NonNull L value) {
            this.value = value;
        }

        @Override
        public <T> T map(@NonNull Function<? super L, ? extends T> left, @NonNull Function<? super R, ? extends T> right) {
            return left.apply(this.value);
        }

        @Override
        public @NonNull Optional<L> left() {
            return this.optional != null ? this.optional : (this.optional = Optional.of(this.value));
        }

        @Override
        public @NonNull Optional<R> right() {
            return Optional.empty();
        }
    }

    private static final class Right<L, R> extends Either<L, R> {
        private final @NonNull R value;
        private @Nullable Optional<R> optional;

        private Right(@NonNull R value) {
            this.value = value;
        }

        @Override
        public <T> T map(@NonNull Function<? super L, ? extends T> left, @NonNull Function<? super R, ? extends T> right) {
            return right.apply(this.value);
        }

        @Override
        public @NonNull Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public @NonNull Optional<R> right() {
            return this.optional != null ? this.optional : (this.optional = Optional.of(this.value));
        }
    }
}
