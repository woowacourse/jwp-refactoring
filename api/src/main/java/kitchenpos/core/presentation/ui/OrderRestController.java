package kitchenpos.core.presentation.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.presentation.dto.OrderCreateRequest;
import kitchenpos.order.presentation.dto.OrderUpdateStatusRequest;
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
    public ResponseEntity<Order> create(@RequestBody final OrderCreateRequest request) {
        final Order created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.findAll())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderUpdateStatusRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request.getOrderStatus()));
    }
}
