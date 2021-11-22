package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderTable(final Order order) {
        validateOrderTable(getOrderTable(order));
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                "빈 상태의 테이블에는 주문을 추가할 수 없습니다.(table id: %d)",
                orderTable.getId()
            ));
        }
    }

    private OrderTable getOrderTable(final Order order) {
        return orderTableRepository.findById(order.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
    }
}
