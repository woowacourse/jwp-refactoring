package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Long create(final Long orderTableId) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.validateIsEmpty();
        Orders orders = new Orders(orderTable, COOKING, LocalDateTime.now());
        return orderRepository.save(orders).getId();
    }

    public List<OrderResponse> list() {
        final List<Orders> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Orders savedOrders = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(COMPLETION, savedOrders.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrders.updateOrderStatus(orderStatus);
        orderRepository.save(savedOrders);
        return OrderResponse.from(savedOrders);
    }

}
