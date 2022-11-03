package kitchenpos.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CustomCollector<T, R, K> implements Collector<T, Map<T, R>, Map<T, R>> {

    private final Collection<R> other;
    private final Function<T, K> currentKeyMapper;
    private final Function<R, K> otherKeyMapper;

    public CustomCollector(Collection<R> other, Function<T, K> currentKeyMapper, Function<R, K> otherKeyMapper) {
        this.other = other;
        this.currentKeyMapper = currentKeyMapper;
        this.otherKeyMapper = otherKeyMapper;
    }

    public static <T, R, K> CustomCollector<T, R, K> associate(
            Collection<R> other,
            Function<T, K> currentKeyMapper,
            Function<R, K> otherKeyMapper
    ) {
        return new CustomCollector<>(other, currentKeyMapper, otherKeyMapper);
    }

    @Override
    public Supplier<Map<T, R>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<T, R>, T> accumulator() {
        return (map, t) -> {
            R r = other.stream()
                    .filter(it -> currentKeyMapper.apply(t).equals(otherKeyMapper.apply(it)))
                    .findAny()
                    .orElse(null);
            map.put(t, r);
        };
    }

    @Override
    public BinaryOperator<Map<T, R>> combiner() {
        return (m1, m2) -> {
            m1.putAll(m2);
            return m1;
        };
    }

    @Override
    public Function<Map<T, R>, Map<T, R>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.IDENTITY_FINISH);
    }

}
