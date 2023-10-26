package kitchenpos.table.domain;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.common.event.OrderCreationEvent;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private static final List<OrderStatus> UNCHANGEABLE_STATUS = List.of(OrderStatus.MEAL, OrderStatus.COOKING);

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;


    public OrderTableValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateChangeableEmpty(Long orderTableId) {
        if (isUnableChangeEmpty(orderTableId)) {
            throw new IllegalArgumentException("Completion 상태가 아닌 주문 테이블은 주문 가능 여부를 변경할 수 없습니다.");
        }
    }

    private boolean isUnableChangeEmpty(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, UNCHANGEABLE_STATUS);
    }

    @EventListener
    public void validateOrderTable(OrderCreationEvent event) {
        Long orderTableId = event.getOrderTableId();

        if (!orderTableRepository.existsById(orderTableId)) {
            throw new NoSuchElementException("ID에 해당하는 주문 테이블을 찾을 수 없습니다.");
        }
        if (orderTableRepository.existsByIdAndEmptyIsTrue(orderTableId)) {
            throw new IllegalArgumentException("주문할 수 없는 상태의 테이블이 존재합니다.");
        }
    }

}
