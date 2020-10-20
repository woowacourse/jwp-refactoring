package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EmptyTest {
    static Stream<Arguments> emptySources() {
        return Stream.of(
                Arguments.of(false, false),
                Arguments.of(true, true)
        );
    }

    @DisplayName("입력한 값과 같은 empty 값인지 비교")
    @ParameterizedTest
    @MethodSource("emptySources")
    void compare(boolean input, boolean expected) {
        Empty empty = Empty.of(false);

        Empty result = empty.compare(input);

        assertThat(result.isEmpty()).isEqualTo(expected);
    }
}
