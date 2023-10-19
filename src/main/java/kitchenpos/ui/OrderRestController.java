package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderUpdateStatusRequest;
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
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

//    @PostMapping
//    public ResponseEntity<Order> create(@RequestBody final Order order) {
//        final Order created = orderService.create(order);
//        final URI uri = URI.create("/api/orders/" + created.getId());
//        return ResponseEntity.created(uri)
//                .body(created)
//                ;
//    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody final OrderCreateRequest request) {
        final Order created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<Order>> readAll() {
        return ResponseEntity.ok()
                .body(orderService.readAll())
                ;
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderUpdateStatusRequest request
    ) {
        return ResponseEntity
                .ok(orderService.changeOrderStatus(orderId, request))
                ;
    }
}
