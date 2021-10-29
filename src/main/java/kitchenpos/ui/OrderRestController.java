package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.application.dtos.OrderRequest;
import kitchenpos.application.dtos.OrderResponse;
import kitchenpos.application.dtos.OrderStatusRequest;
import kitchenpos.domain.Order;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest request) {
        final Order created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        final OrderResponse response = new OrderResponse(created);
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<Order> orders = orderService.list();
        final List<OrderResponse> response = orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest request
    ) {
        final Order order = orderService.changeOrderStatus(orderId, request);
        final OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(response);
    }
}
