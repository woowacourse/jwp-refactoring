package kitchenpos.ordertable.domain;

import kitchenpos.BaseTest;
import kitchenpos.ordertable.exception.OrderTableGuestNumberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GuestNumberTest extends BaseTest {

    @Test
    void 게스트_수를_생성한다() {
        // given
        Integer numberOfGuests = 1;

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new GuestNumber(numberOfGuests));
    }

    @Test
    void 게스트의_수가_null_이면_예외를_던진다() {
        // given
        Integer numberOfGuests = null;

        // when, then
        Assertions.assertThatThrownBy(() -> new GuestNumber(numberOfGuests))
                .isInstanceOf(OrderTableGuestNumberException.class);
    }

    @Test
    void 게스트의_수가_음수이면_예외를_던진다() {
        // given
        Integer numberOfGuests = -1;

        // when, then
        Assertions.assertThatThrownBy(() -> new GuestNumber(numberOfGuests))
                .isInstanceOf(OrderTableGuestNumberException.class);
    }
}
