package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;
import org.junit.jupiter.api.Test;

class OrderTableStatusTest {

    @Test
    void 방문자수가_1명_이상인데_빈테이블인_경우_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> new TableStatus(new Empty(true), new GuestNumber(2)))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_STATUS_INVALID_ERROR);
    }

    @Test
    void 빈테이블로_바꾸면_방문자수는_0명으로_초기화된다() {
        // given
        final var tableStatus = new TableStatus(new Empty(false), new GuestNumber(3));

        // when
        tableStatus.changeEmpty(true);

        // then
        assertAll(
                () -> assertThat(tableStatus.isEmpty()).isTrue(),
                () -> assertThat(tableStatus.getNumberOfGuests()).isZero()
        );
    }

    @Test
    void 빈테이블의_방문자수를_변경하려는_경우_예외를_던진다() {
        // given
        final var tableStatus = new TableStatus(new Empty(true), new GuestNumber(0));

        // when & then
        assertThatThrownBy(() -> tableStatus.changeGuestNumber(5))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_EMPTY_BUT_CHANGE_GUEST_NUMBER_ERROR);
    }
}
