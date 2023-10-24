package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.exception.InvalidOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuDao menuDao;
    private final OrderTableDao orderTableDao;

    public OrderValidator(final MenuDao menuDao, final OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
    }

    public void validate(final Order order) {
        validateOrderLineItemHasDistinctMenu(order.getOrderLineItems());
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new InvalidOrderException("주문 테이블이 존재하지 않습니다."));
        validateOrderTableIsEmpty(orderTable);
    }

    private void validateOrderLineItemHasDistinctMenu(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new InvalidOrderException("주문 항목의 메뉴는 중복될 수 없습니다.");
        }
    }

    private void validateOrderTableIsEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException("주문 테이블은 비어있을 수 없습니다.");
        }
    }
}
