package kitchenpos.order.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.presentation.dto.OrderCreateRequest;
import kitchenpos.order.presentation.dto.OrderStatusChangeRequest;
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
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.create(request.toRequest());
        URI uri = URI.create("/api/orders/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusChangeRequest request) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request.getOrderStatus()));
    }
}
