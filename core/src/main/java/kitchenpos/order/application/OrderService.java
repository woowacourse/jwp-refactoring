package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableValidator orderTableValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public Long order(final OrderCreateRequest request) {
        final List<Menu> menus = menuRepository.findByIdIn(request.extractMenuIds());
        orderTableValidator.validate(request, menus);
        final List<OrderLineItem> orderLineItems = createOrderLineItems(menus, request);
        final Order order = Order.ofCooking(request.getOrderTableId(), orderLineItems);
        final Order saveOrder = orderRepository.save(order);
        return saveOrder.getId();
    }

    private List<OrderLineItem> createOrderLineItems(final List<Menu> menus, final OrderCreateRequest request) {
        final Map<Long, Long> menuIdAndQuantityMap = request.extractMenuIdAndQuantity();
        return menus.stream()
                .map(menu -> OrderLineItem.of(menu.getName(), menu.getPrice(), menuIdAndQuantityMap.get(menu.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.getById(orderId);
        savedOrder.updateOrderStatus(orderStatusRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }
}
