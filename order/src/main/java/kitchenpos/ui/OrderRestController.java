package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final OrderResponse order = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + order.getId());

        return ResponseEntity.created(uri).body(order);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<OrderResponse> orders = orderService.list();
        return ResponseEntity.ok().body(orders);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                           @RequestBody final OrderUpdateRequest request) {
        final OrderResponse order = orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.ok(order);
    }
}
