package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(null, 0, true);
    }

    @DisplayName("주문 상태 변경 검증 성공")
    @Test
    void validateChangeStatus() {
        Order order = new Order(orderTable, OrderStatus.COOKING.name());
        assertThatCode(order::validateChangeStatus)
                .doesNotThrowAnyException();
    }

    @DisplayName("주문 상태 변경 검증 실패")
    @Test
    void validateChangeStatusFail() {
        Order order = new Order(orderTable, OrderStatus.COMPLETION.name());
        assertThatThrownBy(order::validateChangeStatus)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
