package kitchenpos.domain;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Long orderTableId, OrderLineItems orderLineItems) {
        OrderTable orderTable = getOrderTable(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }

        if (orderLineItems.size() != menuRepository.countByIdIn(orderLineItems.getMenuIds())) {
            throw new IllegalArgumentException("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
