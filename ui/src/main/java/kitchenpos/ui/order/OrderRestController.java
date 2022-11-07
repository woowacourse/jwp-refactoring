package kitchenpos.ui.order;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.dto.response.OrderDto;
import kitchenpos.ui.order.dto.OrderRequestDto;
import kitchenpos.ui.order.dto.OrderStatusRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderDto> create(@RequestBody final OrderRequestDto requestBody) {
        final OrderDto created = orderService.create(requestBody.toCreateOrderDto());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderDto> changeOrderStatus(@PathVariable final Long orderId,
                                                   @RequestBody final OrderStatusRequestDto requestBody) {
        return ResponseEntity.ok(orderService.changeOrderStatus(requestBody.toUpdateOrderStatusDto(orderId)));
    }
}
