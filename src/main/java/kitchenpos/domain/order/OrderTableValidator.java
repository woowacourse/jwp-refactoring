package kitchenpos.domain.order;

import java.util.Arrays;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator implements TableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void ableToChangeEmpty(OrderTable orderTable) {
        orderTable.validateNotInTableGroup();
        validateIsCompletedOrder(orderTable.getId());
    }

    private void validateIsCompletedOrder(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void ableToChangeNumberOfGuests(OrderTable orderTable) {
        orderTable.validateNotEmpty();
    }

    @Override
    public void ableToUngroup(TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
