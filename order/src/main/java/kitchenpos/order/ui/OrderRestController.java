package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.ChangeOrderStatusResponse;
import kitchenpos.order.application.dto.CreateOrderResponse;
import kitchenpos.order.application.dto.SearchOrderResponse;
import kitchenpos.order.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.order.ui.dto.CreateOrderRequest;
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

    @PostMapping("/api/orders")
    public ResponseEntity<CreateOrderResponse> create(@RequestBody CreateOrderRequest request) {
        CreateOrderResponse created = orderService.create(request.toCommand());
        URI uri = URI.create("/api/orders/" + created.id());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<SearchOrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<ChangeOrderStatusResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final ChangeOrderStatusRequest request
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(request.toCommand(orderId)));
    }
}
