package kitchenpos.order.service;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.service.TableEmptyChangeService;
import org.springframework.stereotype.Component;

@Component
public class TableEmptyChangeServiceImpl implements TableEmptyChangeService {

    private final OrderRepository orderRepository;

    public TableEmptyChangeServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(OrderTable orderTable, boolean empty) {
        Order order = orderRepository.getByOrderTable(orderTable);
        if (order.orderStatus() == OrderStatus.COMPLETION) {
            throw new IllegalStateException("계산 완료된 주문의 테이블의 주문 가능 상태를 변경할 수 없다.");
        }
        orderTable.changeEmpty(empty);
    }
}
