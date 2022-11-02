package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.repository.OrderTableRepository;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
        validateEmptyOrderTableId(orderTableId);
        validateInvalidOrderTableId(orderTableId);
        validateOrderLineItems(orderLineItems);
    }

    public void validateChangeStatus(Order order) {
        if (OrderStatus.isCompletion(order.getOrderStatus())) {
            throw new IllegalArgumentException("이미 완료된 주문 상태를 변경할 수 없습니다.");
        }
    }

    private void validateEmptyOrderTableId(final Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    public void validateInvalidOrderTableId(final Long orderTableId) {
        orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }

    private void validateOrderLineItems(final List<OrderLineItemDto> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
    }

}
