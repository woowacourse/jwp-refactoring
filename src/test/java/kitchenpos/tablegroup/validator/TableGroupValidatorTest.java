package kitchenpos.tablegroup.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.tablegroup.exception.NotCompleteTableUngroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TableGroupValidatorTest {

    private final OrderValidator orderValidator = Mockito.mock(OrderValidator.class);
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final TableGroupValidator tableGroupValidator = new TableGroupValidator(orderRepository);

    private final Price price = new Price(new BigDecimal(1000));
    private final Quantity quantity = new Quantity(1L);

    @DisplayName("Order의 상태가 COMPLETION이 아닌 OrderTable이 포함된 TableGroup을 해제하려하면 예외를 발생시킨다.")
    @Test
    void validateOrderStatuses_Exception_NotCompleteOrder() {
        doNothing()
                .when(orderValidator)
                .validateCreation(any(), any());
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem("메뉴1", price, quantity));
        Order order1 = Order.newOrder(orderTableId1, orderLineItems, orderValidator);
        Order order2 = Order.newOrder(orderTableId2, orderLineItems, orderValidator);
        order2.changeOrderStatus(OrderStatus.COOKING);
        when(orderRepository.findAllByOrderTableIdIn(List.of(1L, 2L)))
                .thenReturn(List.of(order1, order2));

        assertThatThrownBy(() -> tableGroupValidator.validateOrderStatuses(List.of(orderTableId1, orderTableId2)))
                .isInstanceOf(NotCompleteTableUngroupException.class);
    }
}
