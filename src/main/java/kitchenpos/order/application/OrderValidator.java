package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository,
                          OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Long orderTableId, List<OrderLineItem> orderLineItems) {
        checkExistOrderTable(orderTableId);
        checkOrderLineItemsIsNotNull(orderLineItems);
        checkExistMenu(orderLineItems);
    }

    private void checkOrderLineItemsIsNotNull(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkExistMenu(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkExistOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
