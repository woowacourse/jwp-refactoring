package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderLineItemResponse;
import kitchenpos.order.ui.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest request) {
        final Order created = orderService.create(request);
        final OrderResponse body = createResponse(created);
        final URI uri = URI.create("/api/orders/" + body.getId());
        return ResponseEntity.created(uri).body(body);
    }

    private OrderResponse createResponse(final Order order) {
        final List<OrderLineItemResponse> items = order.getOrderLineItems().stream()
                .map(item -> new OrderLineItemResponse(item.getMenuName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(), order.getOrderedTime(),
                items);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<OrderResponse> body = orderService.list().stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(body);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest order
    ) {
        OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus().toUpperCase());
        final Order changedOrder = orderService.changeOrderStatus(orderId, orderStatus);
        final OrderResponse body = createResponse(changedOrder);
        return ResponseEntity.ok(body);
    }
}
