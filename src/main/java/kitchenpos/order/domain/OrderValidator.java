package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreate(Long orderTableId, List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderException("주문 목록이 비어있는 경우 주문하실 수 없습니다.");
        }
        if (orderTableRepository.getById(orderTableId).isEmpty()) {
            throw new OrderException("비어있는 테이블에서는 주문할 수 없습니다.");
        }
    }
}
