package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderResponses;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
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
    public OrderResponse create(final OrderRequest request) {
        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemsWith(request));
        orderLineItems.checkSize(menuRepository.countByIdIn(orderLineItems.getMenuIds()));

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.checkValidity();

        final Order savedOrder = orderRepository.save(orderWith(orderTable));

        orderLineItems.updateOrderId(savedOrder);
        savedOrder.updateOrderLineItems(orderLineItems);
        orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems());

        return new OrderResponse(savedOrder);
    }

    public OrderResponses list() {
        return new OrderResponses(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus.name());

        return new OrderResponse(savedOrder);
    }

    private Order orderWith(OrderTable orderTable) {
        return Order.builder()
                .orderTableId(orderTable.getId())
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now())
                .build();
    }

    private OrderLineItem orderLineItemWith(OrderLineItemRequest orderLineItem, Menu menu) {
        return OrderLineItem.builder()
                .menuId(orderLineItem.getMenuId())
                .quantity(orderLineItem.getQuantity())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice())
                .build();
    }

    private List<OrderLineItem> orderLineItemsWith(OrderRequest request) {
        final List<Long> menuIds = request.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> menus = menuRepository.findAllById(menuIds);
        final Map<Long, Menu> menuStorage = menus.stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));
        return request.getOrderLineItems().stream()
                .map(it -> orderLineItemWith(it, menuStorage.get(it.getMenuId())))
                .collect(Collectors.toList());
    }
}
