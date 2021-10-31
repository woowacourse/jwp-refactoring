package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.application.mapper.OrderMapper;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderMapper orderMapper, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        Order order = orderMapper.mapFrom(orderRequest);
        order.register(orderValidator);

        orderRepository.save(order);
        orderLineItemRepository.saveAll(order.getOrderLineItems().toList());
        return order;
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();
        for (final Order order : orders) {
            order.setOrderLineItems(new OrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId())));
        }
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest statusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeStatus(OrderStatus.valueOf(statusRequest.getOrderStatus()));
        orderRepository.save(savedOrder);
        savedOrder.setOrderLineItems(new OrderLineItems(orderLineItemRepository.findAllByOrderId(orderId)));
        return savedOrder;
    }
}
