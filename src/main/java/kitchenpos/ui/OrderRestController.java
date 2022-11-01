package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {
        OrderResponse created = orderService.create(orderRequest);
        URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Void> changeOrderStatus(@PathVariable Long orderId,
                                                  @RequestBody OrderStatusRequest orderRequest) {
        orderService.changeOrderStatus(orderId, orderRequest);
        return ResponseEntity.ok().build();
    }
}
