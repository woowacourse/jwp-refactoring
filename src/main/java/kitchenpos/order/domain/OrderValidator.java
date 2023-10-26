package kitchenpos.order.domain;

import java.util.NoSuchElementException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(
            MenuRepository menuRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrderLineItems orderLineItems, Long orderTableId) {
        validateOrderLineItems(orderLineItems);
        validateOrderTable(orderTableId);
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        boolean hasNotExistingMenu = orderLineItems.getOrderLineItems()
                .stream()
                .noneMatch(orderLineItem -> menuRepository.existsByNameAndPrice(orderLineItem.getName(),
                        orderLineItem.getPrice()));

        if (hasNotExistingMenu) {
            throw new IllegalArgumentException("주문 상품에 존재하지 않는 메뉴가 존재합니다.");
        }
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = findOrderTable(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문할 수 없는 상태의 테이블이 존재합니다.");
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 주문 테이블을 찾을 수 없습니다."));
    }

}
