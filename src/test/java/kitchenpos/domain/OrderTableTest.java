package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("테이블이 비어있으면 예외가 발생한다.")
    @Test
    void validateEmpty() {
        OrderTable orderTable = new OrderTable(1L);

        assertThatThrownBy(orderTable::validateEmpty)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체석이 비어있으면 예외가 발생한다.")
    @Test
    void validateTableGroup() {
        OrderTable orderTable = new OrderTable(1L);

        assertThatThrownBy(orderTable::validateTableGroup)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("0명 미만의 손님이 등록되어 있으면 예외가 발생한다.")
    @Test
    void validateGuest() {
        OrderTable orderTable = new OrderTable(1L, null, -1, true);

        assertThatThrownBy(orderTable::validateGuest)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체석을 비운다.")
    @Test
    void clearTableGroup() {
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable(1L, tableGroup, -1, true);

        orderTable.clearTableGroup();

        assertThat(orderTable.getTableGroup()).isNull();
    }
}
