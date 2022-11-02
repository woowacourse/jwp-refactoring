package kitchenpos.order.presentation;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.presentation.dto.request.OrderRequest;
import kitchenpos.order.presentation.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
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
                .collect(toList());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    @ResponseStatus(OK)
    public OrderResponse changeOrderStatus(@PathVariable Long orderId,
                                           @RequestBody OrderRequest orderRequest) {

        final Order changedOrder = orderService.changeOrderStatus(orderId, orderRequest);

        return OrderResponse.from(changedOrder);
    }
}
