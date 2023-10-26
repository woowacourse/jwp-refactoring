package kitchenpos.order.application.validator;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.application.validator.OrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidatorImpl implements OrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateCompletion(final OrderTable orderTable) {
        if (!orderRepository.existsByOrderTableIdAndCompletion(orderTable.getId())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateAllCompletion(final OrderTables orderTables) {
        final Long completionOrderCount = orderRepository.countCompletionOrderByOrderTableIds(orderTables.getIds());
        orderTables.validateSize(completionOrderCount.intValue());
    }
}
