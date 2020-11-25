package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest order) {
        final OrderResponse orderResponse = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + orderResponse.getId());
        return ResponseEntity.created(uri)
            .body(orderResponse)
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
        @RequestBody final OrderChangeRequest order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }
}
