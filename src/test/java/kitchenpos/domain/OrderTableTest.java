package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.fixtures.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블이 비어있으면 주문이 불가능하다")
    void validateOrderable() {
        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);

        assertThatThrownBy(orderTable::validateOrderable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체가 지정되어 있으면 단체로 지정하지 못한다")
    void validateGroupableWithAlreadyGrouping() {
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(1L, 3);

        assertThatThrownBy(orderTable::validateGroupable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블에 이미 손님이 있다면 단체로 지정하지 못한다")
    void validateGroupableWithEmptyOrderTable() {
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 3);

        assertThatThrownBy(orderTable::validateGroupable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
