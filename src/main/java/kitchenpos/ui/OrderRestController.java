package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.OrderRequest;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.create(orderRequest.toCommand());
        final URI uri = URI.create("/api/orders/" + orderResponse.id());
        return ResponseEntity.created(uri)
                .body(orderResponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final Order order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }
}
