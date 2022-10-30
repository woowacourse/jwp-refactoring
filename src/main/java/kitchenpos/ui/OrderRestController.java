package kitchenpos.ui;

import static org.springframework.http.HttpStatus.OK;

import java.util.stream.Collectors;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {

        final Order created = orderService.create(orderRequest);

        final URI uri = URI.create("/api/orders/" + created.getId());
        final OrderResponse orderResponse = OrderResponse.from(created);

        return ResponseEntity.created(uri)
                .body(orderResponse);
    }

    @GetMapping("/api/orders")
    @ResponseStatus(OK)
    public List<OrderResponse> list() {

        final List<Order> orders = orderService.list();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    @ResponseStatus(OK)
    public OrderResponse changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderRequest orderRequest) {

        final Order changedOrder = orderService.changeOrderStatus(orderId, orderRequest);

        return OrderResponse.from(changedOrder);
    }
}
