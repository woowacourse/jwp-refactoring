package kitchenpos.service;

import static kitchenpos.common.exception.ExceptionType.DUPLICATED_ORDER_LINE_ITEM;
import static kitchenpos.common.exception.ExceptionType.EMPTY_ORDER_TABLE;
import static java.util.stream.Collectors.toSet;

import kitchenpos.common.exception.CustomException;
import kitchenpos.common.exception.ExceptionType;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.service.OrderTableService;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableService orderTableService;

    public OrderValidator(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    void validate(Order order) {
        validateNotEmptyOrderTable(order.geOrderTableId());
        validateOrderLineItems(order.getOrderLineItems());
    }

    private void validateNotEmptyOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableService.getById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new CustomException(EMPTY_ORDER_TABLE);
        }
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateNotEmpty(orderLineItems);
        validateUniqueMenu(orderLineItems);
    }

    private void validateNotEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new CustomException(ExceptionType.EMPTY_ORDER_LINE_ITEMS);
        }
    }

    private void validateUniqueMenu(List<OrderLineItem> orderLineItems) {
        Set<OrderMenu> uniqueMenus = orderLineItems.stream()
                                                   .map(OrderLineItem::getOrderMenu)
                                                   .filter(Objects::nonNull)
                                                   .collect(toSet());

        if (uniqueMenus.size() != orderLineItems.size()) {
            throw new CustomException(DUPLICATED_ORDER_LINE_ITEM);
        }
    }
}
