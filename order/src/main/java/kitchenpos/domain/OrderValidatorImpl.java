package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator{

    private final OrderRepository orderRepository;

    public OrderValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusInCookingOrMeal(final OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("현재 요리중이거나 식사 중인 경우 그룹해제를 할 수 없습니다.");
        }
    }
}
