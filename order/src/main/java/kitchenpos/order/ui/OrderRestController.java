package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.order.dto.request.OrderCreationRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.dto.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.OrderService;
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
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreationRequest request) {
        OrderResponse response = orderService.create(request);
        URI uri = URI.create("/api/orders/" + response.getId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }

}
