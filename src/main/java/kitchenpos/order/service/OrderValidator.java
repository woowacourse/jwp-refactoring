package kitchenpos.order.service;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        OrderLineItems orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.collectMenuIds();
        if (!orderLineItems.hasSameSize(menuRepository.countByIdIn(menuIds))) {
            throw new IllegalArgumentException();
        }

        if (!order.getOrderTable().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
