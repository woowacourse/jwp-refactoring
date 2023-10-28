package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.order.vo.OrderStatus;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.response.OrderResponse;
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
    public ResponseEntity<Void> create(@RequestBody final OrderCreateRequest request) {
        final Long id = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + id);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatus requestOrderStatus
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, requestOrderStatus));
    }
}
