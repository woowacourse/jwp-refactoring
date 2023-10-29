package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final MenuRepository menuRepository;

    public OrderValidatorImpl(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validatePrepare(final OrderLineItems orderLineItems) {
        validateMenuSize(getMenuIds(orderLineItems));
        validateOrderLineItemsEmpty(orderLineItems);
    }

    private void validateMenuSize(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 상품 목록 개수와 실제 메뉴 개수와 같지 않습니다. 주문할 수 있는 상품인지 확인해주세요.");
        }
    }

    private List<Long> getMenuIds(final OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private void validateOrderLineItemsEmpty(final OrderLineItems orderLineItems) {
        if (orderLineItems.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
    }
}
