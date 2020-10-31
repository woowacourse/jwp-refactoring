package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest request) {
        final OrderResponse created = orderService.create(request);
        return ResponseEntity.created(URI.create("/api/orders/" + created.getId()))
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<OrderResponses> list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }
}
