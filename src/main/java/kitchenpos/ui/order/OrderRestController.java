package kitchenpos.ui.order;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.request.OrderCreateRequest;
import kitchenpos.application.order.request.OrderUpdateRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.ui.order.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest request) {
        final Order order = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + order.getId());
        return ResponseEntity.created(uri).body(OrderResponse.from(order));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<Order> orders = orderService.list();
        return ResponseEntity.ok().body(OrderResponse.of(orders));
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest request
    ) {
        Order order = orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.ok().body(OrderResponse.from(order));
    }
}
