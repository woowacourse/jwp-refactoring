package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.validator.OrderValidator;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.UpdateOrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;

    public OrderService(final OrderRepository orderRepository,
                        final OrderValidator orderValidator,
                        final MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);
        final Order order = Order.create(
                orderRequest.getOrderTableId(),
                orderLineItems,
                orderValidator);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> createOrderLineItems(final OrderRequest orderRequest) {
        orderRequest.validate();
        final Map<Long, OrderedMenu> menus = findMenus(orderRequest);
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final long quantity = orderLineItemRequest.getQuantity();
            final OrderedMenu orderedMenu = menus.get(orderLineItemRequest.getMenuId());
            orderLineItems.add(new OrderLineItem(null, quantity, orderedMenu));
        }
        return orderLineItems;
    }

    private Map<Long, OrderedMenu> findMenus(final OrderRequest orderRequest) {
        List<Long> menuIds = orderRequest.getMenuIds();
        orderValidator.validateExistMenus(menuIds);
        return menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(
                        Menu::getId,
                        menu -> new OrderedMenu(
                                menu.getId(),
                                menu.getName(),
                                menu.getPrice())));
    }

    public OrderResponse changeOrderStatus(final Long orderId,
                                           final UpdateOrderStatusRequest updateOrderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(updateOrderStatusRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}
