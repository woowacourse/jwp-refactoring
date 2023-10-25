package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.request.OrderCreateRequest;
import kitchenpos.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest request) {
        Order created = orderService.create(request);
        URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(OrderResponse.from(created));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> response = orderService.list().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatus orderStatus
    ) {
        OrderResponse response = OrderResponse.from(orderService.changeOrderStatus(orderId, orderStatus));
        return ResponseEntity.ok(response);
    }
}
