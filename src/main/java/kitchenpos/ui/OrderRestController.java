package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderStatusChangeRequest;
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
    public ResponseEntity<Void> create(@RequestBody final OrderCreateRequest orderCreateRequest) {
        final Long orderId = orderService.create(
                orderCreateRequest.getMenuIds(),
                orderCreateRequest.getQuantities(),
                orderCreateRequest.getOrderTableId()
        );
        final URI uri = URI.create("/api/orders/" + orderId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Void> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest orderStatusChangeRequest
    ) {
        orderService.changeOrderStatus(orderId, orderStatusChangeRequest.getOrderStatus());
        return ResponseEntity.ok().build();
    }
}
