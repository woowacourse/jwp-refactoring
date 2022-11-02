package kitchenpos.core.table.application;

import java.util.NoSuchElementException;
import kitchenpos.core.order.application.OrderValidator;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.table.domain.OrderTable;
import kitchenpos.core.table.domain.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderCreationValidator implements OrderValidator {

    private final OrderTableDao orderTableDao;

    public OrderCreationValidator(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void validate(final Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(NoSuchElementException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비워져있으면 주문을 생성할 수 없습니다.");
        }
    }
}
