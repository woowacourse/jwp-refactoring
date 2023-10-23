package kitchenpos.ui;

import java.net.URI;
import javax.validation.Valid;
import kitchenpos.application.OrderService;
import kitchenpos.dto.order.OrderChangeStatusRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrdersResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid final OrderCreateRequest request) {
        OrderResponse response = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<OrdersResponse> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderChangeStatusRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
    }
}
