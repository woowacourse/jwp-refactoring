package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateOrderLineItems(OrderLineItems orderLineItems) {
        List<Long> menuIds = extractMenuIds(orderLineItems);

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 상품에 존재하지 않는 메뉴가 존재합니다.");
        }
    }

    private List<Long> extractMenuIds(OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

}
