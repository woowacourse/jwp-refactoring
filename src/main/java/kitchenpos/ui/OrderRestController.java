package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Orders;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderUpdateRequest;
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

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Orders> create(@RequestBody OrderCreateRequest request) {
        Orders savedOrders = orderService.create(request);
        URI uri = URI.create("/api/orders/" + savedOrders.getId());
        
        return ResponseEntity.created(uri)
                .body(savedOrders);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Orders>> list() {
        List<Orders> responses = orderService.list();
        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Orders> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }
}
