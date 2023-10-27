package kitchenpos.domain.order;

import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    public void validate(final Order order) {
        validateOrderLineItems(order);
        validateEmptyOrderTable(order);
    }

    private void validateOrderLineItems(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("[ERROR] 주문할 아이템을 최소 1개 이상 선택해 주세요!");
        }

        final int distinctMenuIdNumber = (int) orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .distinct().count();

        if (orderLineItems.size() != distinctMenuIdNumber) {
            throw new IllegalArgumentException("[ERROR] 주문할 아이템이 같은 메뉴라면, 수량으로 개수를 표시해 주세요.");
        }
    }

    private void validateEmptyOrderTable(final Order order) {
        final OrderTable orderTable = order.getOrderTable();

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블에서는 주문을 할 수 없습니다.");
        }
    }
}
