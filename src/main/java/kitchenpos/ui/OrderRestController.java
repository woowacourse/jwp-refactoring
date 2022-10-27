package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest order) {
        final Order created = orderService.create(order);

        final URI uri = URI.create("/api/orders/" + created.getId());
        final OrderResponse body = OrderResponse.from(created);
        return ResponseEntity.created(uri).body(body);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<Order> foundOrders = orderService.list();

        final List<OrderResponse> body = OrderResponse.from(foundOrders);
        return ResponseEntity.ok().body(body);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                           @RequestBody final OrderStatusRequest order) {
        final Order changedOrder = orderService.changeOrderStatus(orderId, order);

        final OrderResponse body = OrderResponse.from(changedOrder);
        return ResponseEntity.ok(body);
    }
}
