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

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void 테이블_상태를_변경한다(boolean empty) {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 5, !empty);

        // when
        orderTable.changeEmpty(empty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
    }

    @Test
    void 연관된_테이블_그룹이_존재하면_테이블_상태를_변경할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 5, true);

        // when && then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("테이블그룹에 속한 테이블의 상태를 변경할 수 없습니다.");
    }
}
