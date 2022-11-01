package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.ui.dto.CreateOrderRequest;
import kitchenpos.ui.dto.ChangeOrderStatusRequest;
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
    public ResponseEntity<OrderDto> create(@RequestBody final CreateOrderRequest createOrderRequest) {
        final OrderDto created = orderService.create(createOrderRequest.toCreateOrderDto());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final ChangeOrderStatusRequest changeOrderStatusRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus())));
    }
}
