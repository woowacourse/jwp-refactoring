package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderChangeRequestDto;
import kitchenpos.application.dto.OrderCreateRequestDto;
import kitchenpos.domain.Order;
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
    public ResponseEntity<Order> create(@RequestBody final OrderCreateRequestDto dto) {
        final Order created = orderService.create(dto);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderChangeRequestDto dto
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, dto));
    }
}
