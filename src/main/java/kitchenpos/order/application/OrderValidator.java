package kitchenpos.order.application;

import kitchenpos.order.domain.vo.OrderLineItems;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateTable(Long orderTableId) {
        OrderTable orderTable = getOrderTable(orderTableId);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에 주문을 등록할 수 없습니다.");
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 정보가 존재하지 않습니다."));
    }

    public void validate(Long orderTableId, OrderLineItems orderLineItems) {
        validateTable(orderTableId);
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문의 메뉴가 존재하지 않습니다.");
        }
    }
}
