package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.OrderStatusValidator;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidatorImpl implements OrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateUngroup(final List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                mapToOrderTableIds(orderTables), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 조리, 식사 상태면 해제할 수 없습니다.");
        }
    }

    private static List<Long> mapToOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void validateChangeEmpty(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 테이블의 주문 상태가 조리, 식사라면 상태를 변경할 수 없습니다.");
        }
    }
}
