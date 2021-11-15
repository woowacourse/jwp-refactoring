package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
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
        validateOrderTable(order);
        validateOrderItems(order);
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
