package kitchenpos.order.service;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository,
                          OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
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

        OrderTable orderTable = findOrderTableById(order.getOrderTableId());
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTableById(Long tableId) {
        return orderTableRepository.findById(tableId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
