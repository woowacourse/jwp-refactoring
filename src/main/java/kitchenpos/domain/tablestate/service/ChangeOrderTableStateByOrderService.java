package kitchenpos.domain.tablestate.service;

import java.util.List;
import kitchenpos.config.DomainComponent;
import kitchenpos.domain.exception.InvalidOrderStatusCompletionException;
import kitchenpos.domain.exception.InvalidTableGroupException;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.service.ChangeOrderTableStateService;

@DomainComponent
public class ChangeOrderTableStateByOrderService implements ChangeOrderTableStateService {

    private final OrderRepository orderRepository;

    public ChangeOrderTableStateByOrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void changeEmpty(final OrderTable orderTable, final boolean empty) {
        if (orderTable.isUngrouping()) {
            throw new InvalidTableGroupException();
        }

        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        for (final Order order : orders) {
            validateOrderStatus(order);
        }

        orderTable.changeEmptyStatus(empty);
    }

    private void validateOrderStatus(final Order order) {
        if (!order.isCompletion()) {
            throw new InvalidOrderStatusCompletionException();
        }
    }
}
