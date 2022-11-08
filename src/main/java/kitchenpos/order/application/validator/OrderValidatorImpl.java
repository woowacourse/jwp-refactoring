package kitchenpos.order.application.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.dao.OrderTableDao;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableDao orderTableDao;

    public OrderValidatorImpl(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void validate(final Order order) {
        validateOrderTableNotEmpty(order);
        validateOrderLineItems(order.getOrderLineItems());
    }

    private void validateOrderTableNotEmpty(final Order order) {
        orderTableDao.getById(order.getOrderTableId());
    }

    public void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
