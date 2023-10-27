package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 주문_테이블_생성() {
        assertDoesNotThrow(
                () -> new OrderTable(null, 0, true)
        );
    }

    @Test
    void 테이블_그룹_수정() {
        final OrderTable orderTable = new OrderTable(null, 0, true);
        final Long updateTableId = 1L;

        orderTable.updateTableGroup(updateTableId);

        assertThat(orderTable.getTableGroupId()).isEqualTo(updateTableId);
    }

    @Test
    void 테이블_인원수_수정() {
        final OrderTable orderTable = new OrderTable(null, 10, false);

        assertDoesNotThrow(
                () -> orderTable.updateNumberOfGuests(20)
        );
    }

    @Test
    void 비어있는_테이블_인원수_수정_시_예외_발생() {
        final OrderTable orderTable = new OrderTable(null, 0, true);

        assertThatThrownBy(
                () -> orderTable.updateNumberOfGuests(10)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100000})
    void 테이블_인원수를_음수로_수정_시_예외_발생(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable(null, 0, false);

        assertThatThrownBy(
                () -> orderTable.updateNumberOfGuests(numberOfGuests)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인원 수는 음수일 수 없습니다.");
    }

    @Test
    void 테이블_상태_수정() {
        final OrderTable orderTable = new OrderTable(null, 0, false);

        assertDoesNotThrow(
                () -> orderTable.updateEmpty(true)
        );
    }

    @Test
    void 테이블_그룹이_있는_테이블_상태_수정_시_예외_발생() {
        final OrderTable orderTable = new OrderTable(1L, 0, true);

        assertThatThrownBy(
                () -> orderTable.updateEmpty(false)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 속해있는 table group이 있습니다.");
    }
}
