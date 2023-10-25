package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.request.CreateOrderRequest;
import kitchenpos.order.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest createOrderRequest) {
        OrderResponse created = orderService.create(createOrderRequest);
        URI uri = URI.create("/api/orders/" + created.getId());
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

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest updateOrderStatusRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, updateOrderStatusRequest));
    }
}
