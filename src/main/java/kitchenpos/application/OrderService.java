package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository, OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        return OrderResponse.from(
                orderRepository.save(
                        new Order(request.getOrderTableId(), mapToOrderLineItems(request.getOrderLineItems()),
                                orderValidator))
        );
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

    private List<OrderLineItem> mapToOrderLineItems(List<OrderLineItemRequest> requests) {
        var result = new ArrayList<OrderLineItem>();
        for (OrderLineItemRequest request : requests) {
            var menuId = request.getMenuId();
            if (!menuRepository.existsById(menuId)) {
                throw new IllegalArgumentException("주문 항목의 메뉴가 존재하지 않습니다. menuId = " + menuId);
            }
            result.add(new OrderLineItem(menuId, request.getQuantity()));
        }
        return result;
    }
}
