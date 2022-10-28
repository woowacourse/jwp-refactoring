package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.dto.request.response.OrderResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest orderCreateRequest) {
        final Order created = orderService.create(orderCreateRequest.toOrder());
        final OrderResponse orderResponse = OrderResponse.from(created);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(orderResponse)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        final List<Order> orders = orderService.list();
        final List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(orderResponses)
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusUpdateRequest orderStatusUpdateRequest
    ) {
        final Order order = orderService.changeOrderStatus(orderId, orderStatusUpdateRequest.toOrder());
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
