package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderCreateRequestDto;
import kitchenpos.order.dto.OrderCreateResponseDto;
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
    public ResponseEntity<OrderCreateResponseDto> create(@RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        final OrderCreateResponseDto created = orderService.create(orderCreateRequestDto);
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
        @RequestBody final Order order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }
}
