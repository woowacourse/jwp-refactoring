package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableEmptyChangedEvent;
import kitchenpos.infra.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableEmptyChangedEventHandler {

    private final OrderRepository orderRepository;

    public TableEmptyChangedEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableEmptyChangedEvent event) {
        OrderTable orderTable = event.orderTable();
        Order order = orderRepository.getByOrderTableId(orderTable.id());

        if (order.orderStatus() == OrderStatus.COMPLETION) {
            throw new IllegalStateException("계산 완료된 주문의 테이블의 주문 가능 상태를 변경할 수 없다.");
        }
    }
}
