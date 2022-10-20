package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.ui.dto.reqeust.OrderChangeStatusRequest;
import kitchenpos.ui.dto.reqeust.OrderCreateRequest;
import kitchenpos.ui.dto.response.OrderResponse;
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

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final var created = orderService.create(request);
        final var orderCreateResponse = OrderResponse.from(created);
        final var uri = URI.create("/api/orders/" + created.getId());

        return ResponseEntity.created(uri)
                .body(orderCreateResponse)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final var orders = orderService.list();
        final var orderResponses = OrderResponse.from(orders);

        return ResponseEntity.ok()
                .body(orderResponses)
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderChangeStatusRequest request
    ) {
        final var changedOrder = orderService.changeOrderStatus(orderId, request);
        final var orderResponse = OrderResponse.from(changedOrder);

        return ResponseEntity.ok(orderResponse);
    }
}
