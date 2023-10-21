package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.ordertable.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @Test
    @DisplayName("테이블 그룹에 이미 속해있는 주문 테이블의 상태를 변경할 수 없다.")
    void notExistingTable() {
        final OrderTable notEmptyTable = new OrderTable(new NumberOfGuests(6), false);
        notEmptyTable.setTableGroupId(6L);

        assertThatThrownBy(() -> notEmptyTable.setEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이라면 손님 수 변경할 수 없다.")
    void emptyTable() {
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(0), true);

        assertThatThrownBy(
                () -> orderTable.setNumberOfGuests(new NumberOfGuests(6)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
