package kitchenpos.domain.order;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems().getItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목 목록이 있어야 합니다.");
        }

        List<Long> menuIds = getMenuIds(orderLineItems);
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("등록되지 않은 메뉴를 주문할 수 없습니다.");
        }

        OrderTable orderTable = orderTableRepository.getById(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블인 경우 주문을 할 수 없습니다.");
        }
    }

    private List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(toList());
    }
}
