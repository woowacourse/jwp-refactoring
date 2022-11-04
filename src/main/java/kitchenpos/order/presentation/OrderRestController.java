package kitchenpos.order.presentation;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderStatusUpdateRequest;
import kitchenpos.order.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/orders")
@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody final OrderRequest orderRequest) {
        final Order created = orderService.create(orderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusUpdateRequest orderStatusUpdateRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatusUpdateRequest));
    }
}
