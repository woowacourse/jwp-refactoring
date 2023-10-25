package kitchenpos.domain.order.service;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderCreateService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public Order create(Long tableId, Order order) {

        OrderTable orderTable = orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        orderTable.createdOrder(order);

        return orderRepository.save(order);
    }
}
