package kitchenpos.order.ui;

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

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrderStatusResponse;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        final Order created = orderService.create(orderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());

        return ResponseEntity.created(uri).body(OrderResponse.from(created));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<Order> orders = orderService.list();

        List<OrderResponse> orderResponses = orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(orderResponses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderStatusResponse> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final OrderStatusRequest orderStatusRequest
    ) {
        Order order = orderService.changeOrderStatus(orderId, orderStatusRequest);
        return ResponseEntity.ok(OrderStatusResponse.from(order));
    }
}
