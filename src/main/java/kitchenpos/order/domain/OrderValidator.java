package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.repository.OrderTableRepository;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(final CreateOrderRequest request) {
        orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }

}
