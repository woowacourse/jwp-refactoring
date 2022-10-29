package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.ui.apiservice.OrderApiService;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderRequest;
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

    private final OrderApiService orderApiService;

    public OrderRestController(final OrderApiService orderApiService) {
        this.orderApiService = orderApiService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {
        final OrderResponse created = orderApiService.create(orderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderApiService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderChangeStatusRequest request
    ) {
        return ResponseEntity.ok(orderApiService.changeOrderStatus(orderId, request));
    }
}
