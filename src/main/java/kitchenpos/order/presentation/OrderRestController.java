package kitchenpos.order.presentation;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.dto.request.OrderCreateRequest;
import kitchenpos.order.domain.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.domain.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final Order created = orderService.create(request.toEntity());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(OrderResponse.toDTO(created))
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> response = orderService.list().stream()
                .map(OrderResponse::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response)
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest request
    ) {
        return ResponseEntity.ok(OrderResponse.toDTO(orderService.changeOrderStatus(orderId, request.getOrderStatus())));
    }
}
