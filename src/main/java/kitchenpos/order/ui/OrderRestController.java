package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderStatusModifyRequest;
import kitchenpos.order.application.dto.response.OrderQueryResponse;
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
    public ResponseEntity<OrderQueryResponse> create(@RequestBody final OrderCreateRequest request) {
        final OrderQueryResponse created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderQueryResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderQueryResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusModifyRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }
}
