package kitchenpos.domain;

import static kitchenpos.exception.NumberOfGuestsExceptionType.NEGATIVE_VALUE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.NumberOfGuestsException;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    void 손님_수가_음수이면_예외가_발생한다() {
        // given
        int value = -1;

        // when
        BaseExceptionType exceptionType = assertThrows(NumberOfGuestsException.class, () ->
                new NumberOfGuests(value)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(NEGATIVE_VALUE_EXCEPTION);
    }

    @Test
    void 값이_같으면_동등한_객체이다() {
        // given
        NumberOfGuests numberOfGuests = new NumberOfGuests(10);
        NumberOfGuests other = new NumberOfGuests(10);

        // expect
        assertThat(numberOfGuests).isEqualTo(other);
    }
}
