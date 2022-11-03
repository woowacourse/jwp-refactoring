package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.exception.OrderLineItemMenuException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TableValidator tableValidator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository, final TableValidator tableValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public Order create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItem> orderLineItems = mapToOrderLineItems(orderCreateRequest);
        validateOrderLineItemsMenuIdExists(orderLineItems);
        validateTable(orderCreateRequest.getOrderTableId());
        final Order order = Order.of(orderCreateRequest.getOrderTableId(), orderLineItems);
        return orderRepository.save(order);
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

    private void validateTable(final Long orderTableId) {
        tableValidator.validateTableNotExists(orderTableId);
        tableValidator.validateTableEmpty(orderTableId);
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
