package kitchenpos.domain.order;

import kitchenpos.domain.menu.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderLineItemValidator {

    private final MenuRepository menuRepository;

    public OrderLineItemValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateOrderMenuExist(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문항목에 존재하지 않는 메뉴가 있습니다. 주문을 등록할 수 없습니다.");
        }
    }
}
