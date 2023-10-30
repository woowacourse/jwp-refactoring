package kichenpos.ordertable;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.controller.dto.OrderChangeStatusRequest;
import kitchenpos.order.controller.dto.OrderCreateRequest;
import kitchenpos.order.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderTableRestController {
    private final OrderService orderService;

    public OrderTableRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Order> create(@RequestBody final OrderCreateRequest orderCreateRequest) {
        final Order created = orderService.create(orderCreateRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderChangeStatusRequest orderChangeStatusRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderChangeStatusRequest));
    }
}
