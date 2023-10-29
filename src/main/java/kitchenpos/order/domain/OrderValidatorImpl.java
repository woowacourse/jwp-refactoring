package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validatePrepare(final Long orderTableId, final OrderLineItems orderLineItems) {
        validateOrderTable(orderTableId);
        validateMenuSize(getMenuIds(orderLineItems));
        validateOrderLineItemsEmpty(orderLineItems);
    }

    private void validateOrderTable(final Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블 식별자값으로 주문 테이블을 조회할 수 없습니다.");
        }
    }

    private void validateMenuSize(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 상품 목록 개수와 실제 메뉴 개수와 같지 않습니다. 주문할 수 있는 상품인지 확인해주세요.");
        }
    }

    private List<Long> getMenuIds(final OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private void validateOrderLineItemsEmpty(final OrderLineItems orderLineItems) {
        if (orderLineItems.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
    }
}
