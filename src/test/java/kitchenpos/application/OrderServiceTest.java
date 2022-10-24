package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 테이블을 추가할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable = OrderTableFixture.createDefaultWithoutId();

        // when
    }

    @Test
    void list() {
    }

    @Test
    void changeOrderStatus() {
    }
}
