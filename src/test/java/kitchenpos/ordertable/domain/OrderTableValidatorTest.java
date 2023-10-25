package kitchenpos.ordertable.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.fixtures.OrderTableFixtures;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderTableValidatorTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderTableValidator orderTableValidator;

    @Test
    @DisplayName("주문 테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
    void throws_ExistTableGroup() {
        // given
        final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
        orderTable.updateTableGroupId(1L);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(OrderTableException.AlreadyExistTableGroupException.class)
                .hasMessage("[ERROR] 이미 Table Group이 존재합니다.");
    }

    @Test
    @DisplayName("주문 테이블 ID에 해당하는 주문이 존재하고 주문 상태가 조리 or 식사면 예외가 발생한다.")
    void throws_existsByOrderTableIdAndOrderStatusIn() {
        // given
        final OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final int orderLineItemSize = 1;
        final Order order = Order.from(savedOrderTable.getId(), orderLineItemSize, orderLineItemSize);
        order.changeStatus(OrderStatus.MEAL);
        orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(CannotChangeEmptyStateByOrderStatusException.class)
                .hasMessage("[ERROR] 주문 테이블의 주문 상태가 조리중이거나 식사중일 때 주문 테이블의 상태를 변경할 수 없습니다.");
    }
}
