package kitchenpos.domain.order;

import kitchenpos.domain.menu.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(final Order order) {
        validateOrderLineItemExist(order);
        validateOrderMenuExist(order);
    }

    public void validateOrderMenuExist(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalStateException("주문항목에 존재하지 않는 메뉴가 있습니다. 주문을 등록할 수 없습니다.");
        }
    }

    public void validateOrderLineItemExist(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }
    }
}
