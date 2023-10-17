package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.order.CreateOrderRequest;
import kitchenpos.ui.dto.order.UpdateOrderRequest;
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
    public ResponseEntity<Order> create(@RequestBody final CreateOrderRequest createOrderRequest) {
        final Order created = orderService.create(createOrderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        final List<Order> orders = orderService.list();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final UpdateOrderRequest updateOrderRequest
    ) {
        final Order order = orderService.changeOrderStatus(orderId, updateOrderRequest);
        return ResponseEntity.ok(order);
    }
}
