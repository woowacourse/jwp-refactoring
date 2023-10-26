package kitchenpos.order.application;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreator orderCreator;

    public OrderService(
        final OrderRepository orderRepository,
        final OrderCreator orderCreator
    ) {
        this.orderRepository = orderRepository;
        this.orderCreator = orderCreator;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        final Order order = orderCreator.create(orderDto);
        final Order savedOrder = orderRepository.save(order);
        return OrderDto.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderDto::from)
            .collect(toUnmodifiableList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final Order savedOrder = orderRepository.getById(orderId);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        return OrderDto.from(savedOrder);
    }
}
