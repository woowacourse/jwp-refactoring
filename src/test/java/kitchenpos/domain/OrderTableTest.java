package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableTest {

    @DisplayName("[SUCCESS] 주문 테이블 상태를 비어있음으로 변경한다.")
    @Test
    void changeOrderTableEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, false);

        // when
        orderTable.changeOrderTableEmpty();

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("[SUCCESS] 주문 테이블 상태를 비어있지 않음으로 변경한다.")
    void changeOrderTableFull() {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, true);

        // when
        orderTable.changeOrderTableFull();

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }
}
