package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.ui.dto.request.OrderCreateRequest;
import kitchenpos.order.ui.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.ui.dto.response.OrderCreateResponse;
import kitchenpos.order.ui.dto.response.OrderFindAllResponse;
import kitchenpos.order.ui.dto.response.OrderStatusChangeResponse;
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
    public ResponseEntity<OrderCreateResponse> create(@RequestBody @Valid final OrderCreateRequest request) {
        final OrderCreateResponse response = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderFindAllResponse>> list() {
        final List<OrderFindAllResponse> responses = orderService.list();
        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderStatusChangeResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest request) {
        final OrderStatusChangeResponse response = orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.ok(response);
    }
}
