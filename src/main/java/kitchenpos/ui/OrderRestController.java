package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderStatusRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final Order created = orderService.create(request);
        final OrderResponse reseponse = OrderResponse.from(created);
        final URI uri = URI.create("/api/orders/" + reseponse.getId());
        return ResponseEntity.created(uri)
                .body(reseponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<OrderResponse> responses = orderService.list()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest orderStatusRequest) {
        final Order order = orderService.changeOrderStatus(orderId, orderStatusRequest);
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
