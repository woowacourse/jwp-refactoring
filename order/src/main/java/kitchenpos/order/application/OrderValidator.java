package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Order order) {
        validateTable(order.getOrderTableId());
        validateOrderLineItems(order.getOrderLineItems());
    }

    private void validateTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 상품이 존재하지 않습니다.");
        }

        long menuCount = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .distinct()
                .count();
        if (menuCount != orderLineItems.size()) {
            throw new IllegalArgumentException("주문 상품 속 메뉴는 중복되면 안됩니다");
        }
    }
}
