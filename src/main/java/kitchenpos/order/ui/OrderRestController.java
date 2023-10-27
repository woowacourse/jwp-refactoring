package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.CreateOrderDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.UpdateOrderStatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderDto request) {
        OrderDto created = orderService.create(request);
        URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusDto request) {
        UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(orderId, request.getOrderStatus());
        return ResponseEntity.ok(orderService.changeOrderStatus(updateOrderStatusDto));
    }
}
