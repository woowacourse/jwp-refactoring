package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(final Order order) {
        validateOrderTableAvailability(order.getOrderTableId());
        validateAllMenusAvailable(order.getOrderLineItems());
    }

    private void validateOrderTableAvailability(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문하려는 테이블은 비어있을 수 없습니다.");
        }
    }

    private void validateAllMenusAvailable(final List<OrderLineItem> orderLineItems) {
        validateEmptyOrder(orderLineItems);
        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("잘못된 메뉴가 주문에 포함되었습니다.");
        }
    }

    private void validateEmptyOrder(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 메뉴가 비어있을 수 없습니다.");
        }
    }
}
