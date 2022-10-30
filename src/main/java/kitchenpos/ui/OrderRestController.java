package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.request.order.ChangeOrderStatusRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.dto.response.OrderResponse;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final CreateOrderRequest request) {
        final Order created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
            .body(new OrderResponse(created))
            ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> orders = orderService.list().stream()
            .map(it -> new OrderResponse(it))
            .collect(Collectors.toList());

        return ResponseEntity.ok()
            .body(orders)
            ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final ChangeOrderStatusRequest request
    ) {
        Order changed = orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.ok(new OrderResponse(changed))
            ;
    }
}
