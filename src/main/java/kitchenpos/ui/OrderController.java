package kitchenpos.ui;

import javax.validation.Valid;
import kitchenpos.application.OrderService;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderStatusRequest;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody final OrderRequest request) {
        final OrderResponse response = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @Valid @RequestBody final OrderStatusRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }
}
