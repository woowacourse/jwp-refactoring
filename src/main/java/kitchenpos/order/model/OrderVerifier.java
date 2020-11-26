package kitchenpos.order.model;

import java.util.List;

import org.springframework.util.CollectionUtils;

import kitchenpos.domain.OrderTable;
import kitchenpos.orderline.model.OrderLineItem;

public class OrderVerifier {
    public static void validateNotCompleteOrderStatus(List<Order> orders) {
        for (Order order : orders) {
            if (!order.isComplete()) {
                throw new IllegalArgumentException("테이블의 주문이 아직 결제되지 않았습니다.");
            }
        }
    }

    public static void validateOrderCreation(List<OrderLineItem> orderLineItems, int savedMenuCount,
        OrderTable orderTable) {
        validateMinimumMenuCount(orderLineItems);
        validateMenuExistence(orderLineItems, savedMenuCount);
        validateEmptyTable(orderTable);
    }

    private static void validateEmptyTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에는 주문할 수 없습니다.");
        }
    }

    private static void validateMenuExistence(List<OrderLineItem> orderLineItems, int savedMenuCount) {
        if (orderLineItems.size() != savedMenuCount) {
            throw new IllegalArgumentException("존재하지 않는 메뉴로 주문할 수 없습니다.");
        }
    }

    private static void validateMinimumMenuCount(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문은 1개 이상의 메뉴를 포함해야 합니다.");
        }
    }
}
