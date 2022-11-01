package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemCreateRequest;
import kitchenpos.application.dto.request.OrderStatusRequest;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.exception.OrderLineItemMenuException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
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
    public Order create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItem> orderLineItems = mapToOrderLineItems(orderCreateRequest);
        validateOrderLineItemsMenuIdExists(orderLineItems);
        final OrderTable orderTable = getOrderTable(orderCreateRequest);
        final Order order = Order.of(orderTable, orderLineItems);
        return orderRepository.save(order);
    }

    private OrderTable getOrderTable(final OrderCreateRequest orderCreateRequest) {
        return orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
    }

    private static List<OrderLineItem> mapToOrderLineItems(final OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.getOrderLineItems()
                .stream()
                .map(OrderService::mapToOrderLineItem)
                .collect(Collectors.toList());
    }

    private static OrderLineItem mapToOrderLineItem(final OrderLineItemCreateRequest orderLineItemCreateRequest) {
        return new OrderLineItem(orderLineItemCreateRequest.getMenuId(), orderLineItemCreateRequest.getQuantity());
    }

    private void validateOrderLineItemsMenuIdExists(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = mapToMenuIds(orderLineItems);
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new OrderLineItemMenuException();
        }
    }

    private static List<Long> mapToMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus);
        return savedOrder;
    }
}
