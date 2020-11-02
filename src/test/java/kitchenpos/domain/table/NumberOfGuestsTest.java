package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NumberOfGuestsTest {

    @DisplayName("생성자 테스트 - IAE 발생, Price가 0보다 작은 경우")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3})
    void constructor_NumberOfGuestsLessThanZero_ThrownIllegalArgumentException(int number) {
        assertThatThrownBy(() -> new NumberOfGuests(number))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자 테스트 - 성공, Price가 0보다 크거나 같음")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void constructor_NumberOfGuestsMoreThanOrEqualZero_Success(int expectedNumber) {
        NumberOfGuests numberOfGuests = new NumberOfGuests(expectedNumber);

        assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(expectedNumber);
    }
}
