package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.UpdateOrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        final Order order = Order.create(orderTable, orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> createOrderLineItems(final OrderRequest orderRequest) {
        orderRequest.validate();
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        final Map<Long, Menu> menus = findMenus(orderRequest);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final Menu menu = menus.get(orderLineItemRequest.getMenuId());
            final OrderLineItem orderLineItem = new OrderLineItem(null, menu, orderLineItemRequest.getQuantity());
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    private Map<Long, Menu> findMenus(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getMenuIds();
        final List<Menu> menus = menuRepository.findByIdIn(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
        return menus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));
    }

    public OrderResponse changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest updateOrderStatusRequest) {
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
