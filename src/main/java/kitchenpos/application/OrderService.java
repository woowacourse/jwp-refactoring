package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.service.OrderCreateService;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderCreateService orderCreateService;

    public OrderService(OrderRepository orderRepository, OrderCreateService orderCreateService) {
        this.orderRepository = orderRepository;
        this.orderCreateService = orderCreateService;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        Order order = request.toEntity();
        Order saved = orderRepository.save(order.create(orderCreateService));
        return OrderResponse.of(saved);
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    // @Transactional
    // public Order changeOrderStatus(final Long orderId, final Order order) {
    //     final Order savedOrder = orderRepository.findById(orderId)
    //             .orElseThrow(IllegalArgumentException::new);
    //
    //     if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
    //         throw new IllegalArgumentException();
    //     }
    //
    //     final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
    //     savedOrder.setOrderStatus(orderStatus.name());
    //
    //     orderRepository.save(savedOrder);
    //
    //     savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
    //
    //     return savedOrder;
    // }
}
