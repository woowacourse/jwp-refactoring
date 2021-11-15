package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validateOrderLineItem(order);
        validateOrderTable(order);
        validateOrderItems(order);
    }

    private void validateOrderLineItem(Order order) {
        if (order.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
    }

    private void validateOrderTable(Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에선 주문할 수 없습니다.");
        }
    }

    private void validateOrderItems(Order order) {
        order.getOrderLineItems().forEach(it -> {
            if (!menuRepository.existsById(it.getMenuId())) {
                throw new IllegalArgumentException("존재하지 않는 메뉴로 주문할 수 없습니다.");
            }
        });
    }
}
