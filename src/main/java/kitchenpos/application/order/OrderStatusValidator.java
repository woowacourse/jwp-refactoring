package kitchenpos.application.order;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.table.TableValidator;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator implements TableValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void checkOrderTableStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 주문 테이블은 비활성화할 수 없습니다.");
        }
    }

    @Override
    public void checkOrderStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("아직 조리중이거나 식사중인 주문 테이블이 포함되어 있습니다.");
        }
    }
}
