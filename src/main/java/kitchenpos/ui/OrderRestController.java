package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.order.Order;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrdersResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        final Order order = orderService.create(orderRequest);
        final OrderResponse orderResponse = OrderResponse.of(order);
        return ResponseEntity.created(URI.create("/api/orders/" + order.getId())).body(orderResponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<OrdersResponse> list() {
        final List<Order> orders = orderService.list();
        final OrdersResponse ordersResponse = OrdersResponse.of(orders);
        return ResponseEntity.ok().body(ordersResponse);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                           @RequestBody final OrderRequest orderRequest) {
        final Order order = orderService.changeOrderStatus(orderId, orderRequest);
        final OrderResponse orderResponse = OrderResponse.of(order);
        return ResponseEntity.ok(orderResponse);
    }
}
