package kitchenpos.domain;

import java.util.List;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        validateMenuExists(orderLineItems);
        validateOrderTable(orderTableId);
    }

    private void validateMenuExists(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .toList();

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }
    }

    private void validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }
}
