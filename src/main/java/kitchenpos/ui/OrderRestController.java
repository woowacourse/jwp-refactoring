package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {

        final Order created = orderService.create(orderRequest);

        final URI uri = URI.create("/api/orders/" + created.getId());
        final OrderResponse orderResponse = OrderResponse.from(created);

        return ResponseEntity.created(uri)
                .body(orderResponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {

        final List<Order> orders = orderService.list();

        final List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(orderResponses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderRequest orderRequest) {

        final Order changedOrder = orderService.changeOrderStatus(orderId, orderRequest);

        final OrderResponse orderResponse = OrderResponse.from(changedOrder);

        return ResponseEntity.ok(orderResponse);
    }
}
