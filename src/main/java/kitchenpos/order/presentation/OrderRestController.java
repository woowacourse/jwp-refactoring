package kitchenpos.order.presentation;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.UpdateOrderStatusRequestDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.presentation.dto.OrderRequest;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        final OrderResponse created = orderService.create(orderRequest.toServiceDto());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final UpdateOrderStatusRequestDto orderStatusRequestDto
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatusRequestDto));
    }
}
