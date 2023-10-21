package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @Test
    @DisplayName("테이블 그룹에 이미 속해있는 주문 테이블의 상태를 변경할 수 없다.")
    void notExistingTable() {
        final OrderTable notEmptyTable = new OrderTable(); // 비어있음 상태를 변경하기 위한 객체
        notEmptyTable.setTableGroupId(3L);

        assertThatThrownBy(() -> notEmptyTable.setEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경하려는 손님 수가 음수라면 변경할 수 없다.")
    void invalidGuestNumber() {
        final OrderTable orderTable = new OrderTable();

        assertThatThrownBy(
                () -> orderTable.setNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이라면 손님 수 변경할 수 없다.")
    void emptyTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        assertThatThrownBy(
                () -> orderTable.setNumberOfGuests(6))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
