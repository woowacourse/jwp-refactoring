package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        return OrderResponse.from(orderRepository.save(mapToOrder(request)));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        var order = orderRepository.getById(orderId);
        order.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private Order mapToOrder(OrderRequest request) {
        return new Order(request.getOrderTableId(), mapToOrderLineItems(request.getOrderLineItems()), orderValidator);
    }

    private List<OrderLineItem> mapToOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::mapToOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemRequest request) {
        var menuId = request.getMenuId();
        if (!menuRepository.existsById(menuId)) {
            throw new IllegalArgumentException("주문 항목의 메뉴가 존재하지 않습니다. menuId = " + menuId);
        }
        return new OrderLineItem(menuId, request.getQuantity());
    }
}
