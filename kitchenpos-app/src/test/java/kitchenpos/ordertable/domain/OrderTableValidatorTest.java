package kitchenpos.ordertable.domain;

import static kitchenpos.common.fixtures.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.fixtures.MenuFixtures.MENU1_PRICE;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.common.fixtures.OrderTableFixtures;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.OrderTableRepository;
import kitchenpos.domain.orertable.OrderTableValidator;
import kitchenpos.domain.orertable.TableGroup;
import kitchenpos.domain.orertable.TableGroupRepository;
import kitchenpos.domain.orertable.exception.OrderTableException;
import kitchenpos.domain.orertable.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
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
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableValidator orderTableValidator;

    @Test
    @DisplayName("주문 테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
    void throws_ExistTableGroup() {
        // given
        final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        orderTable.updateTableGroup(savedTableGroup);

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
        final OrderLineItem orderLineItem = new OrderLineItem(MENU1_NAME, MENU1_PRICE, 1L);
        final Order order = Order.from(savedOrderTable.getId(), orderLineItemSize, orderLineItemSize, List.of(orderLineItem));
        order.changeStatus(OrderStatus.MEAL);
        orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(CannotChangeEmptyStateByOrderStatusException.class)
                .hasMessage("[ERROR] 주문 테이블의 주문 상태가 조리중이거나 식사중일 때 주문 테이블의 상태를 변경할 수 없습니다.");
    }
}
