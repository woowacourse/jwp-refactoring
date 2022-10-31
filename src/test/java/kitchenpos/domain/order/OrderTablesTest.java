package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("주문 테이블은 공석이어야 합니다.")
    @Test
    void constructWithNonEmptyOrderTable() {
        final var orderTables = List.of(
                makeOrderTable(1, true),
                makeOrderTable(2, false)
        );

        assertThatThrownBy(() -> new OrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있지 않은 주문 테이블이 존재합니다.");
    }

    private OrderTable makeOrderTable(final int numberOfGuests, final boolean empty) {
        return new OrderTable(new GuestCount(numberOfGuests), empty);
    }
}
