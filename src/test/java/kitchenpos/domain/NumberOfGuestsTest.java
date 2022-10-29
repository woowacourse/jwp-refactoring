package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    void createNumberOfGuests() {
        // given
        int count = 2;
        // when
        NumberOfGuests numberOfGuests = new NumberOfGuests(count);
        // then
        assertThat(numberOfGuests).isNotNull();
    }

    @Test
    void createNumberOfGuestsWithZeroNumber() {
        // given
        int count = 0;
        // when
        NumberOfGuests numberOfGuests = new NumberOfGuests(count);
        // then
        assertThat(numberOfGuests).isNotNull();
    }

    @Test
    void createNumberOfGuestsWithNegativeNumber() {
        // given
        int count = -2;
        // when
        assertThatThrownBy(() -> new NumberOfGuests(count))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
