package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderLineItemMenuException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.menu.domain.Menu;
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
        validateTable(orderCreateRequest.getOrderTableId());
        final Order order = Order.of(orderCreateRequest.getOrderTableId(), orderLineItems);
        return orderRepository.save(order);
    }

    private List<OrderLineItem> mapToOrderLineItems(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItemCreateRequest> orderLineItemCreateRequests = orderCreateRequest.getOrderLineItems();
        validateDuplicated(orderLineItemCreateRequests);
        return orderLineItemCreateRequests
                .stream()
                .map(this::mapToOrderLineItem)
                .collect(Collectors.toList());
    }

    private void validateDuplicated(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        final long distinctCount = countDistinct(orderLineItemCreateRequests);
        if (distinctCount != orderLineItemCreateRequests.size()) {
            throw new OrderLineItemMenuException();
        }
    }

    private long countDistinct(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        return orderLineItemCreateRequests.stream()
                .mapToLong(OrderLineItemCreateRequest::getMenuId)
                .distinct()
                .count();
    }

    private OrderLineItem mapToOrderLineItem(final OrderLineItemCreateRequest orderLineItemCreateRequest) {
        final Menu menu = menuRepository.findById(orderLineItemCreateRequest.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        return new OrderLineItem(menu.getUpdatableMenuInfo(), orderLineItemCreateRequest.getQuantity());
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
