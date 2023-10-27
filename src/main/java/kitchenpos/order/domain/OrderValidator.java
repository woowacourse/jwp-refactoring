package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateInit(final Order order) {
        validateEmptyTable(order);
        validateEmptyOrderLineItems(order);
        validateInvalidMenu(order);
    }

    private void validateEmptyTable(final Order order) {
        final OrderTable orderTable = findOrderTableById(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있어 주문을 할 수 없습니다.");
        }
    }

    private OrderTable findOrderTableById(final long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    private void validateEmptyOrderLineItems(final Order order) {
        if (CollectionUtils.isEmpty(order.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 품목이 없어 주문할 수 없습니다.");
        }
    }

    private void validateInvalidMenu(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        final List<Long> menuIds = extractMenuIds(orderLineItems);
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 또는 동일한 메뉴가 포함되어 있습니다.");
        }
    }

    private List<Long> extractMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }
}
