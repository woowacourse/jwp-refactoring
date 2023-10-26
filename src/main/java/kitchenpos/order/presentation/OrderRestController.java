package kitchenpos.order.presentation;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.presentation.dto.OrderCreateRequest;
import kitchenpos.order.presentation.dto.OrderStatusChangeRequest;
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

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Void> create(@RequestBody final OrderCreateRequest orderCreateRequest) {
        final Long orderId = orderService.create(
                orderCreateRequest.getMenuIds(),
                orderCreateRequest.getQuantities(),
                orderCreateRequest.getOrderTableId()
        );
        final URI uri = URI.create("/api/orders/" + orderId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Void> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest orderStatusChangeRequest
    ) {
        orderService.changeOrderStatus(orderId, orderStatusChangeRequest.getOrderStatus());
        return ResponseEntity.ok().build();
    }
}
