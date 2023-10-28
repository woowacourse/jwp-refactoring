package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Long create(final Long orderTableId) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.validateIsEmpty();
        Order order = new Order(OrderStatus.COOKING, LocalDateTime.now());
        return orderRepository.save(order).getId();
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.validateStatusIsEqualToCompletion();

        savedOrder.updateOrderStatus(orderStatus);
        orderRepository.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }

}
