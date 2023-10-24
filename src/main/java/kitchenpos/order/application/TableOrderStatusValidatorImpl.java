package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableOrderStatusValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TableOrderStatusValidatorImpl implements TableOrderStatusValidator {

    private final OrderRepository orderRepository;

    public TableOrderStatusValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateIsTableCompleteMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateIsTableGroupCompleteMeal(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
