package kitchenpos.adapter.presentation.web;

import static kitchenpos.adapter.presentation.web.OrderRestController.*;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderResponse;

@RequestMapping(API_ORDERS)
@RestController
public class OrderRestController {
    public static final String API_ORDERS = "/api/orders";

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final OrderResponse created = orderService.create(request);
        final URI uri = URI.create(API_ORDERS + "/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }
    //
    // @PutMapping("/{orderId}/order-status")
    // public ResponseEntity<Order> changeOrderStatus(
    //         @PathVariable final Long orderId,
    //         @RequestBody final Order order
    // ) {
    //     return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    // }
}
