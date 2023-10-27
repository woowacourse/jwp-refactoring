package kitchenpos.order.domain.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderLineItem;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        OrderTable orderTable = findByOrderTable(order.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);

        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        List<Menu> savedMenus = findMenus(orderLineItems);
        validateMenusExist(orderLineItems, savedMenus);
    }

    private OrderTable findByOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }
    }

    private List<Menu> findMenus(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        return menuRepository.findAllById(menuIds);
    }

    private void validateMenusExist(List<OrderLineItem> orderLineItems, List<Menu> savedMenus) {
        if (orderLineItems.size() != savedMenus.size()) {
            throw new IllegalArgumentException("올바르지 않은 메뉴입니다.");
        }
    }
}
