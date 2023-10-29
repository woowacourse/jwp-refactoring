package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.request.UpdateOrderStatusRequest;
import kitchenpos.order.application.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        final OrderResponse created = orderService.create(orderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<OrderResponse> allOrder = orderService.list();
        return ResponseEntity.ok()
                .body(allOrder);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                           @RequestBody final UpdateOrderStatusRequest updateOrderStatusRequest) {
        final OrderResponse orderResponse = orderService.changeOrderStatus(orderId, updateOrderStatusRequest);
        return ResponseEntity.ok()
                .body(orderResponse);
    }
}
