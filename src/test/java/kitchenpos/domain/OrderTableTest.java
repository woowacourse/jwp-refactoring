package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @ParameterizedTest
    @ValueSource(ints = {3, 6, 10})
    void 테이블_인원을_변경한다(int numberOfGuests) {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 5, false);

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void 테이블_인원을_음수로_변경할_수_없다(int numberOfGuests) {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 5, false);

        // when && then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("테이블 인원은 양수여야합니다.");
    }

    @Test
    void 테이블이_비어있으면_변경이_불가능하다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 5, true);

        // when && then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("비어있는 테이블의 인원을 변경할 수 없습니다.");
    }
}
