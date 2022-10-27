package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Test
    void 주문_테이블을_생성한다() {
        // given
        final long tableGroupId = 1L;
        final int numberOfGuests = 5;
        final boolean empty = false;

        // when, then
        assertThatCode(() -> new OrderTable(tableGroupId, numberOfGuests, empty))
                .doesNotThrowAnyException();
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 5, true);
        final int expected = 10;

        // when
        orderTable.changeNumberOfGuests(expected);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    @Test
    void 테이블_지정이_되어있다면_true를_반환한다() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 5, true);

        // when
        final boolean actual = orderTable.isTableGroupMapped();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 테이블_지정이_null이라면_false를_반환한다() {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, true);

        // when
        final boolean actual = orderTable.isTableGroupMapped();

        // then
        assertThat(actual).isFalse();
    }
}
