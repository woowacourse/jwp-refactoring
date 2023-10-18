package kitchenpos.ui.v1;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderControllerV1 {

    private final OrderService orderService;

    public OrderControllerV1(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest request) {
        var created = orderService.create(request);
        URI uri = URI.create("/api/v1/orders/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok()
            .body(orderService.findAll());
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
        @PathVariable Long orderId,
        @RequestBody OrderUpdateRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request.getOrderStatus()));
    }
}
