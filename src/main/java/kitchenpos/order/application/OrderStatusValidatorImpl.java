package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderDto;
import kitchenpos.product.domain.OrderStatus;
import kitchenpos.table.application.OrderStatusValidator;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusValidatorImpl implements OrderStatusValidator {

    private final OrderDao orderDao;

    public OrderStatusValidatorImpl(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public boolean existsByIdAndStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses) {
        final OrderDto savedOrder = orderDao.findByOrderTableId(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        return anyMatchStatus(orderStatuses, savedOrder);
    }

    @Override
    public boolean existsByIdInAndStatusIn(final List<Long> orderTableIds, final List<OrderStatus> orderStatuses) {
        return orderDao.findAllByOrderTableIdIn(orderTableIds).stream()
                .anyMatch(orderDto -> anyMatchStatus(orderStatuses, orderDto));
    }

    private static boolean anyMatchStatus(final List<OrderStatus> orderStatuses, final OrderDto savedOrder) {
        return orderStatuses.stream()
                .anyMatch(orderStatus -> orderStatus.isStatus(savedOrder.getOrderStatus()));
    }
}
