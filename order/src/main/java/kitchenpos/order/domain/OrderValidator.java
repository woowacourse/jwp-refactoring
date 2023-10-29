package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Component;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validateOrderLineItem(order);
        validateOrderTable(order);
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

    private void validateOrderTable(Order order) {
        Long orderTableId = order.getOrderTableId();
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("EMPTY 상태인 테이블에 주문할 수 없습니다.");
        }
    }
}
