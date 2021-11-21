package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.OrderLineItemRequest;
import kitchenpos.application.dtos.OrderRequest;
import kitchenpos.application.dtos.OrderStatusRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemsWith(request));
        orderLineItems.checkSize(menuRepository.countByIdIn(orderLineItems.getMenuIds()));

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.checkValidity();

        final Order savedOrder = orderRepository.save(orderWith(orderTable));

        orderLineItems.updateOrderId(savedOrder);
        savedOrder.updateOrderLineItems(orderLineItems);
        orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems());

        return savedOrder;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus.name());

        return savedOrder;
    }

    private Order orderWith(OrderTable orderTable) {
        return Order.builder()
                .orderTableId(orderTable.getId())
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now())
                .build();
    }

    private OrderLineItem orderLineItemWith(OrderLineItemRequest orderLineItem) {
        return OrderLineItem.builder()
                .menuId(orderLineItem.getMenuId())
                .quantity(orderLineItem.getQuantity())
                .build();
    }

    private List<OrderLineItem> orderLineItemsWith(OrderRequest request) {
        return request.getOrderLineItems().stream()
                .map(this::orderLineItemWith)
                .collect(Collectors.toList());
    }
}
