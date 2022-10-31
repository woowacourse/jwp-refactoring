package kitchenpos.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.OrderService;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.domain.Order;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody final OrderRequest request) {
        final Order created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
            .body(orderService.list());
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final OrderRequest request) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }
}
