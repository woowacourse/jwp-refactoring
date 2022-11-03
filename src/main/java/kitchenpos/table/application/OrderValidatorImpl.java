package kitchenpos.table.application;

import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableDao orderTableDao;

    public OrderValidatorImpl(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void validate(Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 주문을 받을수 없는 상태입니다.");
        }
    }
}
