package kitchenpos.application.order;

import kitchenpos.domain.common.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.ListOrderResponse;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.exception.menu.MenuNotFoundException;
import kitchenpos.exception.order.OrderNotFoundException;
import kitchenpos.exception.order.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public OrderResponse create(final CreateOrderRequest request) {
        final List<Menu> menus = convertToMenus(request);
        final List<Quantity> quantities = convertToQuantities(request);
        final OrderTable orderTable = convertToOrderTable(request);
        final OrderLineItems orderLineItems = OrderLineItems.from(menus, quantities);

        final Order order = Order.of(orderTable, orderLineItems);
        orderLineItems.setOrder(order);
        return OrderResponse.of(orderRepository.save(order));
    }

    private List<Quantity> convertToQuantities(final CreateOrderRequest request) {
        return request.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getQuantity)
                .map(Quantity::of)
                .collect(Collectors.toList());
    }

    private OrderTable convertToOrderTable(final CreateOrderRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new OrderTableNotFoundException(request.getOrderTableId()));
    }

    private List<Menu> convertToMenus(final CreateOrderRequest request) {
        return request.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getMenuId)
                .map(this::checkMenuExists)
                .collect(Collectors.toList());
    }

    private Menu checkMenuExists(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));
    }

    public ListOrderResponse list() {
        return ListOrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = convertToOrder(orderId);
        savedOrder.setOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private Order convertToOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
