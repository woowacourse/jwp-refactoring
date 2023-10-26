package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.ChangeOrderStatusCommand;
import kitchenpos.application.dto.CreateOrderCommand;
import kitchenpos.application.dto.domain.OrderDto;
import kitchenpos.ui.dto.PutOrderStatusRequest;
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
    public ResponseEntity<OrderDto> create(@RequestBody final CreateOrderCommand command) {
        final OrderDto created = orderService.create(command);
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
            @RequestBody final PutOrderStatusRequest order
    ) {
        final ChangeOrderStatusCommand command = new ChangeOrderStatusCommand(orderId, order.getOrderStatus());
        final OrderDto changedOrder = orderService.changeOrderStatus(command);
        return ResponseEntity.ok(changedOrder);
    }

}
