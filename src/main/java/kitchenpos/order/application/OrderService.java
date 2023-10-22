package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderTableRepository orderTableRepository,
            OrderRepository orderRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(it -> new OrderLineItem(menuRepository.getById(it.getMenuId()), it.getQuantity()))
                .toList();
        OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        return OrderResponse.from(orderRepository.save(
                new Order(orderTable, orderLineItems)
        ));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .toList();
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
