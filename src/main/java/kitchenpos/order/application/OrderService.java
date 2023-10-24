package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.MenuSnapShotCreator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderValidator orderValidator;
    private final MenuSnapShotCreator menuSnapShotCreator;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    public OrderService(
            OrderValidator orderValidator,
            MenuSnapShotCreator menuSnapShotCreator,
            OrderRepository orderRepository,
            MenuRepository menuRepository) {
        this.orderValidator = orderValidator;
        this.menuSnapShotCreator = menuSnapShotCreator;
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
                .stream()
                .map(it ->
                        menuSnapShotCreator.create(
                                menuRepository.getById(it.getMenuId()), it.getQuantity()
                        )).collect(Collectors.toList());
        return OrderResponse.from(orderRepository.save(
                new Order(request.getOrderTableId(), orderLineItems, orderValidator)
        ));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusChangeRequest request) {
        Order savedOrder = orderRepository.getById(orderId);
        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());
        orderRepository.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }
}
