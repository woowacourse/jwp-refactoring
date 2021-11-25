package kitchenpos.table.domain;

import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Order order) {
        validate(order, getOrderTable(order));
    }

    private void validate(Order order, OrderTable orderTable) {
        OrderLineItems orderLineItems = order.getOrderLineItems();
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        validateOrderLineItems(orderLineItems);
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getValues())) {
            throw new IllegalArgumentException();
        }

        if (orderLineItems.isDifferentSize(menuRepository.countByIdIn(orderLineItems.getMenuIds()))) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void validateChangeStatus(Order order) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), order.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
