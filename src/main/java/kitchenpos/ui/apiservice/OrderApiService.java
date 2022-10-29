package kitchenpos.ui.apiservice;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderApiService {

    private final OrderService orderService;

    public OrderApiService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        Order order = orderService.create(request.getOrderTableId(), orderLineItems);
        return OrderResponse.of(order);
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderService.list();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        Order order = orderService.changeOrderStatus(orderId, request.getOrderStatus());
        return OrderResponse.of(order);
    }
}
