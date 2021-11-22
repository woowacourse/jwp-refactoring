package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrdersService;
import kitchenpos.domain.Orders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(final OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<Orders> create(@RequestBody final Orders orders) {
        final Orders created = ordersService.create(orders);
        final URI uri = URI.create("/api/orders/" + created.getId());

        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Orders>> list() {
        return ResponseEntity.ok().body(ordersService.list());
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<Orders> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final Orders orders
    ) {
        return ResponseEntity.ok(ordersService.changeOrderStatus(orderId, orders));
    }
}
