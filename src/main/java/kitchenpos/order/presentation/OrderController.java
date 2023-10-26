package kitchenpos.order.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.presentation.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.presentation.dto.request.CreateOrderRequest;
import kitchenpos.order.presentation.dto.response.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody final CreateOrderRequest request) {
        final Order order = orderService.create(request);
        final OrderResponse response = OrderResponse.from(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/api/orders/" + order.getId()))
                             .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAllOrder() {
        final List<Order> orders = orderService.list();
        final List<OrderResponse> responses = OrderResponse.convertToList(orders);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(responses);
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                           @RequestBody final ChangeOrderStatusRequest request) {
        final Order order = orderService.changeOrderStatus(orderId, request);
        final OrderResponse response = OrderResponse.from(order);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(response);
    }
}
