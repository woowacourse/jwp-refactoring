package kitchenpos.table.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    @DisplayName("값이 0 이상이면 정상적으로 NumberOfGuest 객체를 생성할 수 있다.")
    void NumberOfGuests_success() {
        //given, when
        final NumberOfGuests actual = NumberOfGuests.from(0);

        //then
        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("값이 0보다 작을 때 NumberOfGuest 객체를 생성하면 예외를 반환한다.")
    void NumberOfGuests_fail() {
        //given, when
        final ThrowingCallable actual = () -> NumberOfGuests.from(-2);

        //then
        Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}
