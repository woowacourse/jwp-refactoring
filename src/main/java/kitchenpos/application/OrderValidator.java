package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() != menuRepository.countByIdIn(collectMenuIds(orderLineItems))) {
            throw new IllegalArgumentException("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
        }
    }

    private List<Long> collectMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }
}
