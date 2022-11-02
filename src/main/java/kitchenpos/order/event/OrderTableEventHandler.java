package kitchenpos.order.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.order.repository.OrderTableRepository;

@Component
public class OrderTableEventHandler {

    private OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void verifiedOrderTable(VerifiedOrderTableEvent event) {
        orderTableRepository.findById(event.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }

}
