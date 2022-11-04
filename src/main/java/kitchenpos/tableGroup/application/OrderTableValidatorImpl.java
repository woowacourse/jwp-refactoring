package kitchenpos.tableGroup.application;

import kitchenpos.order.application.OrderTableValidator;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderTableDao orderTableDao;

    public OrderTableValidatorImpl(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void checkEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] orderTable을 찾을 수 없습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] orderTable이 empty입니다.");
        }
    }
}
