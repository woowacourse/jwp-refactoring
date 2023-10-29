package kitchenpos.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Order order) {
        final OrderTable orderTable = findOrderTableById(order.getOrderTableId());
        validateEmptyByOrderTable(orderTable);
        validateMenuInOrderLineItems(order.getOrderLineItems());
        validateEmptyByOrderLineItems(order.getOrderLineItems());
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(
                        () -> new NoSuchElementException("존재하지 않는 order table 입니다. orderTableId: " + orderTableId));
    }

    private void validateMenuInOrderLineItems(final List<OrderLineItem> orderLineItems) {
        orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .forEach(this::validateExistMenuById);
    }

    private void validateExistMenuById(final Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new NoSuchElementException("존재하지 않는 menu입니다. menuId: " + menuId);
        }
    }

    private void validateEmptyByOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("order table은 비어있을 수 없습니다.");
        }
    }

    private void validateEmptyByOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("order line item 은 1개 이상이어야 합니다.");
        }
    }
}
