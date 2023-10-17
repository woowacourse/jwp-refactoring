package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.application.OrderService;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.CreateOrderRequest;
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

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.create(request);
        URI uri = URI.create("/api/orders/" + response.getId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> findAll() {
        List<OrderResponse> response = orderService.findAll();

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody ChangeOrderStatusRequest request
    ) {
        OrderResponse response = orderService.changeOrderStatus(orderId, request);

        return ResponseEntity.ok(response);
    }
}
