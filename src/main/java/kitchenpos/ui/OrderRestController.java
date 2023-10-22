package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.ui.request.OrderStatusChangeRequest;
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
    public ResponseEntity<Order> create(
            @Valid @RequestBody final OrderCreateRequest request
    ) {
        final var response = orderService.create(request);
        final var uri = URI.create("/api/orders/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        final var response = orderService.list();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @Valid @RequestBody final OrderStatusChangeRequest request
    ) {
        final var response = orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.ok(response);
    }
}
