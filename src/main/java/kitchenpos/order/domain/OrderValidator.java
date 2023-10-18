package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableDao orderTableDao;

    public OrderValidator(MenuRepository menuRepository, OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderTableDao = orderTableDao;
    }

    public void validateCreate(List<OrderLineItem> orderLineItems, Long orderTableId) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderException("주문 목록이 비어있는 경우 주문하실 수 없습니다.");
        }
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new OrderException("주문 메뉴중 존재하지 않는 메뉴가 있습니다.");
        }
        OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new OrderException("비어있는 테이블에서는 주문할 수 없습니다.");
        }
    }
}
