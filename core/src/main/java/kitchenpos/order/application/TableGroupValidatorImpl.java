package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.Order;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.application.TableGroupValidator;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidatorImpl implements TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateUngroupable(List<Long> orderTableIds) {
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        if (orders.stream().anyMatch(Order::isNotCompletion)) {
            throw new IllegalArgumentException("주문이 완료 상태여만 그룹을 삭제할 수 있습니다.");
        }
    }
}
