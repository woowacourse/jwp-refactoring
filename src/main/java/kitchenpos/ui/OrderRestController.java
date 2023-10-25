package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderStatusUpdateRequest;
import kitchenpos.application.response.OrderResponse;
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

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest orderCreateRequest) {
        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        URI uri = URI.create("/api/orders/" + orderResponse.getId());
        return ResponseEntity.created(uri)
                .body(orderResponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest orderStatusUpdateRequest
    ) {
        OrderResponse orderResponse = orderService.changeOrderStatus(orderId, orderStatusUpdateRequest);
        return ResponseEntity.ok().body(orderResponse);
    }
}
