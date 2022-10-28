package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrdersResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        List<OrderLineItem> orderLineItems = toEntity(orderCreateRequest);
        Order order = new Order(orderCreateRequest.getOrderTableId(), OrderStatus.COOKING.name(), orderLineItems);
        validateExistOrderTableInOrder(order);
        orderRepository.save(order);
        return OrderResponse.of(order);
    }

    private List<OrderLineItem> toEntity(OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.getOrderLineItems()
                .stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.getById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(menu, orderLineItemRequest.getQuantity());
    }

    private void validateExistOrderTableInOrder(final Order order) {
        if (!orderTableRepository.existsById(order.getOrderTableId())) {
            throw new IllegalArgumentException();
        }
        OrderTable orderTable = orderTableRepository.getById(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public OrdersResponse list() {
        List<Order> orders = orderRepository.findAll();
        return OrdersResponse.of(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        Order order = orderRepository.getById(orderId);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.of(order);
    }
}
