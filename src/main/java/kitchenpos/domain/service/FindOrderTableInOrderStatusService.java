package kitchenpos.domain.service;

import java.util.List;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class FindOrderTableInOrderStatusService {

    private final OrderRepository orderRepository;

    public FindOrderTableInOrderStatusService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean existByOrderStatus(final Long orderTableId, final List<OrderStatus> orderStatuses) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }
}
