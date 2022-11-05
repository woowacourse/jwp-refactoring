package kitchenpos.order.application;

import static kitchenpos.exception.ExceptionType.NOT_FOUND_MENU_EXCEPTION;
import static kitchenpos.exception.ExceptionType.NOT_FOUND_ORDER_EXCEPTION;
import static kitchenpos.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static kitchenpos.order.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.CustomIllegalArgumentException;
import kitchenpos.menu.domain.JpaMenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.JpaOrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.table.domain.JpaOrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final JpaMenuRepository menuRepository;
    private final JpaOrderRepository orderRepository;
    private final JpaOrderTableRepository orderTableRepository;

    public OrderService(final JpaMenuRepository menuRepository, final JpaOrderRepository orderRepository,
                        final JpaOrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest request) {
        final List<OrderLineItem> orderLineItems = toOrderLineItems(request.getOrderLineItems());
        validMenu(request, orderLineItems);
        validOrderTable(request.getOrderTableId());
        final Order order = orderRepository.save(convertSaveConditionOrder(request.getOrderTableId(), orderLineItems));
        return new OrderResponse(order);
    }

    private void validMenu(final OrderRequest request, final List<OrderLineItem> orderLineItems) {
        if (request.getOrderLineItems().size() != orderLineItems.size()) {
            throw new CustomIllegalArgumentException(NOT_FOUND_MENU_EXCEPTION);
        }
    }

    private List<OrderLineItem> toOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream().map(this::toOrderLineItem).collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        return new OrderLineItem(toOrderMenu(request), request.getQuantity());
    }

    private OrderMenu toOrderMenu(final OrderLineItemRequest data) {
        final Menu menu = menuRepository.findById(data.getMenuId())
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_MENU_EXCEPTION));

        return new OrderMenu(menu.getName(), menu.getPrice());
    }

    private void validOrderTable(final Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
        }
    }

    private Order convertSaveConditionOrder(final Long orderTableId, List<OrderLineItem> orderLineItems) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        return new Order(orderTable, COOKING.name(), LocalDateTime.now(),
                orderLineItems);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = getOrder(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return new OrderResponse(savedOrder);
    }

    private Order getOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_ORDER_EXCEPTION));
    }
}
