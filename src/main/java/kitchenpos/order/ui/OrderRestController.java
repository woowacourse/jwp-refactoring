package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.response.OrderResponse;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final CreateOrderRequest request) {
        final OrderResponse created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());

        return ResponseEntity.created(uri)
            .body(created)
            ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<OrderResponse> orders = orderService.list();

        return ResponseEntity.ok()
            .body(orders)
            ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final ChangeOrderStatusRequest request
    ) {
        final OrderResponse changed = orderService.changeOrderStatus(orderId, request);

        return ResponseEntity.ok(changed)
            ;
    }
}
