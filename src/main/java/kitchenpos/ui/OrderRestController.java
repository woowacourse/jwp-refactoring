package kitchenpos.ui;

import kitchenpos.domain.order.service.OrderService;
import kitchenpos.domain.order.service.dto.OrderCreateRequest;
import kitchenpos.domain.order.service.dto.OrderResponse;
import kitchenpos.domain.order.service.dto.OrderUpateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final OrderResponse created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PatchMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Void> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderUpateRequest request
    ) {
        orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.noContent().build();
    }
}
