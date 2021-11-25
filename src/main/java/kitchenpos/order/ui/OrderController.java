package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
