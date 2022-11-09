package kitchenpos.domain;

import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(null, 5, false);

        // when
        final int expected = 10;
        orderTable.changeNumberOfGuests(expected);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    @Test
    void 주문_테이블의_empty_여부를_변경한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(null, 5, false);

        // when
        orderTable.updateEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void ungroup() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(1L, 5, false);

        // when
        orderTable.ungroup();

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void 단체_지정이_설정되었다면_해당하는_id를_반환한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(1L, 5, false);

        // when
        final Long actual = orderTable.getTableGroupIdOrElseNull();

        // then
        assertThat(actual).isEqualTo(1L);
    }

    @Test
    void 단체_지정이_설정되어있지_않다면_null을_반환한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(null, 5, false);

        // when
        final Long actual = orderTable.getTableGroupIdOrElseNull();

        // then
        assertThat(actual).isNull();
    }

    @Test
    void updateToUsed_empty를_false로_변경한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(null, 5, true);

        // when
        orderTable.updateToUsed();

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void validateNotEmpty_호출시_empty가_true라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(null, 5, true);

        // when, then
        assertThatThrownBy(orderTable::validateNotEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateEmptyUpdatable_주문_테이블에_단체지정이_null이_아니라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = 주문_테이블_생성(1L, 5, false);

        // when, then
        assertThatThrownBy(orderTable::validateEmptyUpdatable)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
