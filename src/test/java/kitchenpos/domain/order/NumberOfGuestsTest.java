package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {
    static Stream<Arguments> numberOfGuestsSource() {
        return Stream.of(
                Arguments.of(10, 10),
                Arguments.of(5, 5)
        );
    }

    @DisplayName("유효하지 않은 값으로 객체 생성시 예외 반환")
    @Test
    void constructNumberOfGuestsWithInvalidTest() {
        assertThatThrownBy(() -> {
            NumberOfGuests.of(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력한 값과 같은 numberOfGuests 값인지 비교")
    @ParameterizedTest
    @MethodSource("numberOfGuestsSource")
    void compare(int input, int expected) {
        NumberOfGuests numberOfGuests = NumberOfGuests.of(10);

        NumberOfGuests result = numberOfGuests.compare(NumberOfGuests.of(input));

        assertThat(result.getNumberOfGuests()).isEqualTo(expected);
    }
}
