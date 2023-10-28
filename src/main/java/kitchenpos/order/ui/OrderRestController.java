package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final OrderResponse response = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> readAll() {
        return ResponseEntity.ok()
                .body(orderService.readAll())
                ;
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderUpdateStatusRequest request
    ) {
        return ResponseEntity
                .ok(orderService.changeOrderStatus(orderId, request))
                ;
    }
}
