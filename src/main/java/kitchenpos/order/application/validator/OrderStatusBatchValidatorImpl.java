package kitchenpos.order.application.validator;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.application.validator.OrderStatusBatchValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusBatchValidatorImpl implements OrderStatusBatchValidator {

    private final OrderRepository orderRepository;

    public OrderStatusBatchValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void batchValidateCompletion(final OrderTables orderTables) {
        final Long completionOrderCount = orderRepository.countCompletionOrderByOrderTableIds(orderTables.getIds());
        orderTables.validateSize(completionOrderCount.intValue());
    }
}
