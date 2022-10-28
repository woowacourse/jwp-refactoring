package kitchenpos.dao;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoUtils {

    public static <T> List<T> asList(final Map<Long, T> entities) {
        return entities.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public static <T> void assertAllEquals(final List<T> actualList, final List<T> expectedList,
                                           final BiConsumer<T, T> assertion) {
        final var expectedSize = actualList.size();
        assertThat(expectedList).hasSize(expectedSize);

        for (int i = 0; i < expectedSize; i++) {
            final var actual = actualList.get(i);
            final var expected = expectedList.get(i);

            assertion.accept(actual, expected);
        }
    }
}
