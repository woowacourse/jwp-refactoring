package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.model.OrderStatus;
import kitchenpos.order.application.dto.OrderCreateRequestDto;
import kitchenpos.order.application.dto.OrderResponseDto;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponseDto> create(@RequestBody final OrderCreateRequestDto orderCreateRequest) {
        final OrderResponseDto orderResponse = orderService.create(orderCreateRequest);
        final URI uri = URI.create("/api/orders/" + orderResponse.getId());
        return ResponseEntity.created(uri)
            .body(orderResponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponseDto>> list() {
        return ResponseEntity.ok()
            .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponseDto> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final OrderStatus orderStatus
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatus));
    }
}
