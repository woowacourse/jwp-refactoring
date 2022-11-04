package kitchenpos.ordertable.support;

import kitchenpos.order.support.OrderTableValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderTableDao orderTableDao;

    public OrderTableValidatorImpl(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public Long validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 생성하기 위한 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비었습니다");
        }

        return orderTable.getId();
    }
}
