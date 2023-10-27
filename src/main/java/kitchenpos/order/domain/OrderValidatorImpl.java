package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(final Order order) {
        OrderTable orderTable = orderTableRepository.getById(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문을 등록할 수 없습니다.");
        }

        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        if (orderLineItems.stream().distinct().count() != orderLineItems.size()) {
            throw new IllegalArgumentException("주문 항목은 각각 다른 메뉴여야합니다.");
        }
    }

}
