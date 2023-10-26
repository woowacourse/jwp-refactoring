package kitchenpos.order.application;

import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.MenuSnapshot;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuSnapshotService menuSnapshotService;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuSnapshotService menuSnapshotService,
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.menuSnapshotService = menuSnapshotService;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        final Long orderTableId = request.getOrderTableId();

        orderValidator.validate(orderTableId);

        final Order.OrderFactory orderFactory = new Order.OrderFactory(orderTableId);
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final MenuSnapshot menuSnapshot = menuSnapshotService.getMenuSnapshotFor(orderLineItemRequest.getMenuId());
            orderFactory.addMenu(menuSnapshot, orderLineItemRequest.getQuantity());
        }
        final Order order = orderFactory.create();

        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest statusChangeRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(statusChangeRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.from(order);
    }
}
