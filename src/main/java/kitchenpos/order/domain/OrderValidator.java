package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        validateOrderLineItem(order);
    }

    public void validateOrderLineItem(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        for (OrderLineItem orderLineItem : orderLineItems) {
            Long menuId = orderLineItem.getMenuId();
            findMenuById(menuId);
        }
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 메뉴를 찾을 수 없습니다."));
    }
}
