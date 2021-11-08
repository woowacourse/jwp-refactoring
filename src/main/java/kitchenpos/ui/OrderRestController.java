package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.ui.dto.OrdersRequest;
import kitchenpos.ui.dto.OrdersResponse;
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
    public ResponseEntity<OrdersResponse> create(@RequestBody final OrdersRequest ordersRequest) {
        final OrdersResponse newOrder = orderService.create(ordersRequest);
        final URI uri = URI.create("/api/orders/" + newOrder.getId());
        return ResponseEntity.created(uri)
                .body(newOrder);
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrdersResponse>> findAll() {
        return ResponseEntity.ok()
                .body(orderService.findAll());
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrdersResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                            @RequestBody final OrdersRequest ordersRequest) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, ordersRequest));
    }
}
