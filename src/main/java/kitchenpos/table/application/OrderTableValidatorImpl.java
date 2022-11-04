package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableValidator;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {
    private final OrderTableDao orderTableDao;

    public OrderTableValidatorImpl(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public Long validOrderTableAndGet(final OrderCreateRequest orderCreateRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateIfTableEmpty(orderTable);
        return orderTable.getId();
    }

    private void validateIfTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
