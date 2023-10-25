package kitchenpos.order.application;

import kitchenpos.EntityFactory;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderTableEventListenerTest {

    @Autowired
    private OrderTableService orderTableService;
    @Autowired
    private EntityFactory entityFactory;

    @Test
    @DisplayName("주문 테이블의 빈 테이블 여부가 변경될 때 주문 상태가 COOKING, MEAL이면 예외가 발생한다")
    void validateOrderStatus() {
        //given
        final OrderTable orderTable = entityFactory.saveOrderTable();
        entityFactory.saveOrder(orderTable);

        final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

        //when, then
        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 조리 중이거나 식사 중입니다.");
    }
}
