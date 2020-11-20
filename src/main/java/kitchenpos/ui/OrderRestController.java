package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static kitchenpos.ui.OrderRestController.ORDER_API;

@RequestMapping(ORDER_API)
@RestController
public class OrderRestController {
    public static final String ORDER_API = "/api/orders";

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody final Order order) {
        final Order created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final Order order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }
}
