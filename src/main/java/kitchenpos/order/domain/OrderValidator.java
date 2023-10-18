package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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

    public void validateCreate(List<OrderLineItem> orderLineItems, OrderTable orderTable) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderException("주문 목록이 비어있는 경우 주문하실 수 없습니다.");
        }
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new OrderException("주문 메뉴중 존재하지 않는 메뉴가 있습니다.");
        }
        if (orderTable.isEmpty()) {
            throw new OrderException("비어있는 테이블에서는 주문할 수 없습니다.");
        }
    }
}
