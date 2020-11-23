package kitchenpos.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderCreateResponse;
import kitchenpos.dto.order.OrderFindAllResponses;
import kitchenpos.dto.order.OrderUpdateStatusRequest;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderCreateResponse> create(@RequestBody final OrderCreateRequest order) {
        final OrderCreateResponse created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<OrderFindAllResponses> findAll() {
        return ResponseEntity.ok()
            .body(orderService.findAll());
    }

    @PatchMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
        @RequestBody final OrderUpdateStatusRequest orderUpdateStatusRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderUpdateStatusRequest));
    }
}
