package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;
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
    public ResponseEntity<Order> create(@RequestBody final OrderCreateRequest request) {
        final Long id = orderService.order(request);
        return ResponseEntity.created(URI.create("/api/orders/" + id)).build();
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> findAll() {
        final List<OrderResponse> orderResponses = orderService.findAll();
        return ResponseEntity.ok().body(orderResponses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest orderStatusRequest
    ) {
        final OrderResponse orderResponse = orderService.changeOrderStatus(orderId, orderStatusRequest);
        return ResponseEntity.ok(orderResponse);
    }
}
