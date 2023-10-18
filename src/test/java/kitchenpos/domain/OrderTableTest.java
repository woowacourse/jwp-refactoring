package kitchenpos.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @ParameterizedTest(name = "변경하려는_게스트_수가_0보다_작다면_예외가_발생한다 = {0}")
    @ValueSource(ints = {-1, Integer.MIN_VALUE})
    void 변경하려는_게스트_수가_0보다_작다면_예외가_발생한다(final int numberOfGuests) {
        // given
        final OrderTable orderTable = new OrderTable(1L, 3, false);

        // expect
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수는 0보다 작을 수 없습니다.");
    }

    @Test
    void 주문_테이블이_비어이있다면_손님_수를_변경할_수_없다() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 3, true);

        // expect
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블에는 손님을 지정할 수 없습니다.");
    }

}
