package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
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
    public ResponseEntity<OrderResponse> respondCreatedOrderResponse(@RequestBody final OrderCreateRequest orderCreateRequest) {
        final OrderResponse created = orderService.createOrder(orderCreateRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());

        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> respondAllOrderResponses() {
        return ResponseEntity.ok()
                .body(orderService.listAllOrders());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> respondChangedOrderResponse(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest orderStatusChangeRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatusChangeRequest));
    }
}
