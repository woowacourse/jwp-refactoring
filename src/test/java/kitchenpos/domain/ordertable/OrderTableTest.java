package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 생성자는_유효한_데이터를_전달하면_orderTable을_초기화한다() {
        // when & then
        assertThatCode(() -> new OrderTable(0, false)).doesNotThrowAnyException();
    }

    @Test
    void 생성자는_손님_수로_음수를_전달하면_예외가_발생한다() {
        // given
        final int invalidNumberOfGuests = -1;

        // when & then
        assertThatThrownBy(() -> new OrderTable(invalidNumberOfGuests, false))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    void changeEmptyStatus_메서드는_전달한_값으로_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        orderTable.changeEmptyStatus(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuests_메서드는_유효한_손님_수를_전달하면_numberOfGuests를_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        orderTable.changeNumberOfGuests(1);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void changeNumberOfGuests_메서드는_orderTable이_비어_있으면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(InvalidEmptyOrderTableException.class);
    }

    @Test
    void isUngrouping_메서드는_group이_되지_않았다면_true를_반환한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when
        final boolean actual = orderTable.isUngrouping();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void isUngrouping_메서드는_group이_되었다면_false를_반환한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        orderTable.group(1L);

        // when
        final boolean actual = orderTable.isUngrouping();

        // then
        assertThat(actual).isFalse();
    }
}
