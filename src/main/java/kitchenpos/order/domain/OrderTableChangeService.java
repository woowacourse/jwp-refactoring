package kitchenpos.order.domain;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.ChangeService;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderTableChangeService implements ChangeService {
    private final OrderRepository orderRepository;

    public OrderTableChangeService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void ungroup(OrderTables orderTables) {
        Orders orders = new Orders(orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds()));
        orders.ungroup(orderTables);
    }

    @Override
    public boolean isCookingOrMeal(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
