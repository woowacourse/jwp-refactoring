package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableDao orderTableDao;

    public OrderValidator(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public void validate(Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 주문을 받을수 없는 상태입니다.");
        }
    }
}
