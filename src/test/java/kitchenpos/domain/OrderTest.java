package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("[예외] 빈 테이블에 대한 주문 생성")
    @Test
    void create_Fail_With_EmptyTable() {
        OrderTable orderTable = createOrderTable(true);

        assertThatThrownBy(
            () -> Order.builder()
                .orderTable(orderTable)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

}