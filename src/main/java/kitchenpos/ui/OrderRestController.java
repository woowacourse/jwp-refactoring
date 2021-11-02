package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderStatusRequest;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest order) {
        final OrderResponse created = orderService.create(order.toEntity());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order.getOrderStatus()));
    }
}
