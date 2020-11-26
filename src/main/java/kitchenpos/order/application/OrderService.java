package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        validateNotEmptyOrderLineItems(orderLineItemRequests);

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        validateSavedMenus(orderLineItemRequests);
        List<OrderLineItem> orderLineItems = convertOrderLineItems(orderLineItemRequests);

        Order order = Order.builder()
            .orderTable(orderTable)
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private void validateNotEmptyOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSavedMenus(final List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
        List<Menu> menus = menuRepository.findAllById(menuIds);

        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> convertOrderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
            .map(this::convertOrderLineItem)
            .collect(Collectors.toList());
    }

    private OrderLineItem convertOrderLineItem(final OrderLineItemRequest request) {
        return OrderLineItem.builder()
            .menuId(request.getMenuId())
            .quantity(request.getQuantity())
            .build();
    }

    @Transactional
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return OrderResponse.listFrom(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(
        final Long orderId,
        final OrderStatusChangeRequest request
    ) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        Order changedOrder = orderRepository.save(savedOrder);
        return OrderResponse.from(changedOrder);
    }
}
