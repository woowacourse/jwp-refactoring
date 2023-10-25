package kitchenpos.order.service;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
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
