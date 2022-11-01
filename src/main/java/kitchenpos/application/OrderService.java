package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrdersResponse;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = orderLineItems(request.getOrderLineItems());
        Order order = request.toEntity(orderLineItems);
        validateExistOrderTable(order);
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> orderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        return new OrderLineItem(menu, request.getQuantity());
    }

    private void validateExistOrderTable(final Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public OrdersResponse list() {
        List<Order> orders = orderRepository.findAll();
        return OrdersResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}
