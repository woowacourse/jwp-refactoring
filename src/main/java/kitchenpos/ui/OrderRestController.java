package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderCreationDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.request.OrderCreationRequest;
import kitchenpos.ui.dto.request.OrderStatusRequest;
import kitchenpos.ui.dto.response.OrderResponse;
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

    @Deprecated
    @PostMapping("/api/orders")
    public ResponseEntity<Order> create(@RequestBody final Order order) {
        final Order created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/v2/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreationRequest orderCreationRequest) {
        final OrderDto created = orderService.create(OrderCreationDto.from(orderCreationRequest));
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(OrderResponse.from(created));
    }


    @Deprecated
    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @GetMapping("/api/v2/orders")
    public ResponseEntity<List<OrderResponse>> getOrders() {
        final List<OrderResponse> orderResponses = orderService.getOrders()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(orderResponses);
    }

    @Deprecated
    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final Order order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }

    @PutMapping("/api/v2/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest orderStatusRequest
    ) {
        return ResponseEntity.ok(
                OrderResponse.from(orderService.changeOrderStatus(orderId, orderStatusRequest.getOrderStatus())));
    }
}
