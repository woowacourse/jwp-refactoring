package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.exception.InvalidOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Order order) {
        validateOrderLineItemHasDistinctMenu(order.getOrderLineItems());
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new InvalidOrderException("주문 테이블이 존재하지 않습니다."));
        validateOrderTableIsEmpty(orderTable);
    }

    private void validateOrderLineItemHasDistinctMenu(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new InvalidOrderException("주문 항목의 메뉴는 중복될 수 없습니다.");
        }
    }

    private void validateOrderTableIsEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException("주문 테이블은 비어있을 수 없습니다.");
        }
    }
}
